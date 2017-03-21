var user_d = null; //detailes of logged in user
var postid; //stores id of current open post
var user_post = null;

//this function is take care when the browser is load at the beginning
$(document).ready(function () {
    user_d = getLocalStorageVal("user"); //get user details from local storage
    if (user_d !== null)
    {
        user_d = JSON.parse(user_d); //make user_d a json object
        updateLocalStorage();
    }else{
        homePage(); //go to home page
    }
    
});

function updateLocalStorage() {
    $.ajax({
        url: "api/user/getUserByID/" + user_d.id,
        type: "GET",
        dataType: "json"
    }).done(function (json) {
        var data = json["data"];
        console.log(data);

        user_d["email"] = data["email"];
        user_d["active"] = data["active"];
        user_d["firstName"] = data["firstName"];
        user_d["lastName"] = data["lastName"];
        user_d["picture"] = data["picture"];
        setLocalStorageVal("user", JSON.stringify(user_d));
        
        homePage(); //go to home page
    })
            .fail(function (xhr, status, errorThrown) {
                console.log("Error: Unable to load user data");
            });
}


//Checks whether the user is logged in
function userIsLoggedIn() {
    return (user_d !== null);
}

//Loads the home page: if user is logged in and activated show posts list, if not activated show message, if not logged in show registration.
function homePage() {
    fillNavBar();
    if (userIsLoggedIn()) {
        if (user_d["active"] === "1") { //if user logged in and ACTIVATED

            fillHomePage();
        } else { //if user is logged in but account is NOT ACTIVATED
            $("#page").html("<div id='activate'>  <a href='javascript:checkDeactiveActivate(); ' >Activate Your Account</a></div> ");
        }
    } else { //if user is not registered
        fillRegister();
    }
}

//Loads everything that's relevant to profile page
function profilePage() {

    fillProfileDetails();

}
//Loads everything that's relevant to the change password page
function changePasswordPage() {
    fillChangePass();
}

// Gets the homePage template, puts it in document, then fills it with add post template and posts list
function fillHomePage() {
    $.ajax({
        url: "templates/homePage.html",
        type: "GET"
    })
            .done(function (html) {
                $("#page").html(html); //put homePage template in div with id  "page" 
                putAddPostTemplate(); //put add post template
                fillPostList(); //fill post list with posts from all users
            })
            .fail(function (xhr, status, errorThrown) {
                console.log("Error: Unable to load postsList");
            });
}

//Gets key and retrieves the value of the key in local storage
function getLocalStorageVal(key) {
    if (typeof (Storage) !== "undefined") {
        // Code for localStorage/sessionStorage.
        return localStorage.getItem(key);
    } else {
        // Sorry! No Web Storage support..
        return null;
    }
}

//Gets key and value and stores it in local storage
function setLocalStorageVal(key, value) {
    if (typeof (Storage) !== "undefined") {
        // Code for localStorage/sessionStorage.
        localStorage.setItem(key, value);
        return true;
    } else {
        // Sorry! No Web Storage support..
        return null;
    }
}
