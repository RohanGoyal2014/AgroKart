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
    if(userAvailable){
        user = firebase.auth().currentUser;
    }else{
        user = {};
    }
});

// function fetchVegetables(){
//     db.ref("/static_sell/vegetables").on("value", function (snapshot) {
//         console.log(snapshot.val());
//         p = snapshot.val();
//         var ans = [], c = 0;
//         for (var key in p) {
//             if (p.hasOwnProperty(key)) {
//                 console.log(key + " -> " + p[key]);
//                 // db.ref("products/" + key).on("value", function (snapshot) {
//                 //     console.log(snapshot.val());
//                 // });
//                 ans[c] = key;
//                 c++;
//             }
//         }
//         console.log(ans);
//         return ans;
//     }, function (error) {
//         console.log(error);
//     });
// }
//
// function fetchFruits(){
//     db.ref("/static_sell/fruits").on("value", function (snapshot) {
//         console.log(snapshot.val());
//         p = snapshot.val();
//         var ans = [], c = 0;
//         for (var key in p) {
//             if (p.hasOwnProperty(key)) {
//                 console.log(key + " -> " + p[key]);
//                 // db.ref("products/" + key).on("value", function (snapshot) {
//                 //     console.log(snapshot.val());
//                 // });
//                 ans[c] = key;
//                 c++;
//             }
//         }
//         console.log(ans);
//         return ans;
//     }, function (error) {
//         console.log(error);
//     });
// }
// function fetchPulses(){
//     db.ref("/static_sell/pulses").on("value", function (snapshot) {
//         console.log(snapshot.val());
//         p = snapshot.val();
//         var ans = [], c = 0;
//         for (var key in p) {
//             if (p.hasOwnProperty(key)) {
//                 console.log(key + " -> " + p[key]);
//                 ans[c] = key;
//                 c++;
//             }
//         }
//         console.log(ans);
//         return ans;
//     }, function (error) {
//         console.log(error);
//     });
// }
//
// function fetchProduct(){
//
// }

app.get("/", function (req, res) {
    res.render("index", {user: user});
});

app.get("/vegetables", function (req, res) {
    console.log("Vegetables!");
    // var list = fetchVegetables();
    res.render("vegetables", {user: user});
});
app.get("/fruits", function (req, res) {
    console.log("Fruits!");
    // var list = fetchFruits();
    res.render("fruits", {user: user});
});
app.get("/pulses", function (req, res) {
    console.log("Pulses!");
    // var list = fetchPulses();
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
        });
    });
    // res.render("product");
    db.ref("/products/" + req.params.name).on("value", function (snapshot) {
        console.log(snapshot.val());
        res.render("product", {object: snapshot.val(), name: req.params.name, user: user});
    });
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
    db.ref("/products/" + req.params.item).orderByChild("farmerEmail").once("value", function (snapshot) {
        console.log(snapshot.val());
        var newQ = snapshot.val();
        var list = new Array();
        for(var key in newQ){
            console.log(key + "=>" + newQ[key]);
            list.push(newQ[key]);
        }

        list[0].quantity -= req.body.quantity;

        db.ref("/products/" + req.params.item).set({
            quantity: list[0].quantity,
        }, function (err) {
            if(err)
                console.log(err);
            else{
                db.ref("/transfers").set({
                    quantity: req.body.quantity,
                    item: req.params.item,
                    paymentType: req.body.group1,
                    email: user.email,
                }, function (err) {
                    if(err)
                        console.log(err);
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