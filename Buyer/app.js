var express = require("express"),
    app = express(),
    bodyParser = require("body-parser"),
    admin = require("firebase-admin"),
    firebase = require("firebase");

var serviceAccount = require("./serviceAccountKey.json");

var config = {
    apiKey: "AIzaSyAkaM9h6vYO1Ev4ed5qmwcQzMJSKnvkZxE",
    authDomain: "agrokart-435cc.firebaseapp.com",
    databaseURL: "https://agrokart-435cc.firebaseio.com",
    projectId: "agrokart-435cc",
    storageBucket: "agrokart-435cc.appspot.com",
    messagingSenderId: "886730815787"
};
firebase.initializeApp(config);

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://agrokart-435cc.firebaseio.com"
});
app.set("view engine", "ejs");
app.use(bodyParser.urlencoded({extended: true}));
app.use(express.static("public"));

var user;
var db = admin.database();
firebase.auth().onAuthStateChanged(function(userAvailable){
    console.log("LOGIN STATE CHANGED!");
    if(userAvailable){
        user = firebase.auth().currentUser;
        console.log("USER THERE: " + JSON.stringify(user));
    }else{
        console.log("USER NTO FOUND");
        user = {};
    }
});

app.get("/", function (req, res) {
    res.render("index", {user: user});
});

app.get("/vegetables", function (req, res) {
    console.log("Vegetables!");
    res.render("vegetables", {user: user});
});
app.get("/fruits", function (req, res) {
    console.log("Fruits!");
    res.render("fruits", {user: user});
});
app.get("/pulses", function (req, res) {
    console.log("Pulses!");
    res.render("pulses", {user: user});
});

app.get("/product/:name", function (req, res) {
    console.log("PRODUCT PAGE");
    var count;
    db.ref("/searches/" + req.params.name).once("value", function (snap) {
        console.log(snap.val());
        if(snap.val())
            count = snap.val().count;
        else
            count = 0;
        count++;
        db.ref("/searches/" + req.params.name).set({
            count : count,
        }, function (err) {
            if(err)
                console.log(err);
            else
                console.log("updated searches count!");
            db.ref("/products/" + req.params.name).on("value", function (snapshot) {
                console.log(snapshot.val());
                res.render("product", {object: snapshot.val(), name: req.params.name, user: user});
            });
        });
    });
    // res.render("product");

});

app.post("/login", function(req, res){
    console.log("sign in clicked!");
    console.log(req.body);
    var email = req.body.email;
    var pass = req.body.password;
    firebase.auth().signInWithEmailAndPassword(email, pass)
        .catch(function (err) {
            console.log("err with signIn!: " + JSON.stringify(err));
            firebase.auth().createUserWithEmailAndPassword(email, pass)
                .catch(function (err) {
                    console.log("err with sign up");
                    res.redirect("/");
                })
                .then(function (user) {
                    console.log("signed up successfully!");
                    res.redirect("/");
                });
        })
        .then(function (user) {
            console.log("user signed in!:" + JSON.stringify(user));
            res.redirect("/");
        });
});

app.post("/buy/:item/:email", function (req, res) {
    console.log("buy called!");
    db.ref("/products/" + req.params.item).orderByChild("farmerEmail").equalTo(req.params.email).once("value", function (snapshot) {
        console.log("SNAPSHOT:" + JSON.stringify(snapshot.val()));
        var newQ = snapshot.val();
        var list = new Array();
        var keyList = new Array();
        for(var key in newQ){
            console.log(key + "=>" + JSON.stringify(newQ[key]));
            list.push(newQ[key]);
            keyList.push(key);
        }

        list[0].qty -= req.body.quantity;

        db.ref("/products/" + req.params.item + "/" + keyList[0]).update({
            qty: list[0].qty,
        }, function (err) {
            if(err)
                console.log(err);
            else{
                console.log("SUCCESSFULLY UPDATED PRODUCTS!");
                db.ref("/transfers").push().set({
                    sellerEmail: req.params.email,
                    quantity: req.body.quantity,
                    item: req.params.item,
                    paymentType: req.body.group1,
                    email: user.email,
                }, function (err) {
                    if(err)
                        console.log("ERror in transaction:" + JSON.stringify(err));
                    else {
                        console.log("transaction successfull");
                        res.redirect("/");
                    }
                });
            }
        });
    });
});


app.listen(3000, function () {
    console.log("server started!");
});