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
firebase.auth().onAuthStateChanged(function(user){
    if(user){
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
    // res.render("product");
    db.ref("/products/" + req.params.name).on("value", function (snapshot) {
        console.log(snapshot.val());
        res.render("product", {object: snapshot.val(), name: req.params.name});
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
                })
                .then(function (user) {
                    console.log("signed up successfully!");
                });
        })
        .then(function (user) {
            console.log("user signed in!:" + JSON.stringify(user));
        });
    res.redirect("/");
});

app.listen(3000, function () {
    console.log("server started!");
});