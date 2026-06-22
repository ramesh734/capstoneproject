function getAuth(){

    let user =
    localStorage.getItem("username");

    let pass =
    localStorage.getItem("password");


    return "Basic " +
    btoa(user + ":" + pass);

}


// Employee Count

fetch("/api/employees", {
    headers: {
        Authorization: getAuth()
    }
})
.then(res => res.json())
.then(data => {
    document.getElementById("employeeCount").innerHTML = data.length;
});




// Asset Count

fetch("/api/assets", {

headers:{
Authorization:getAuth()
}

})
.then(res => res.json())
.then(data => {

document.getElementById(
"assetCount"
).innerHTML = data.length;

});




// Vendor Count

fetch("/api/vendors", {

headers:{
Authorization:getAuth()
}

})
.then(res => res.json())
.then(data => {

document.getElementById(
"vendorCount"
).innerHTML = data.length;

});




// Category Count

fetch("/api/categories", {

headers:{
Authorization:getAuth()
}

})
.then(res => res.json())
.then(data => {

document.getElementById(
"categoryCount"
).innerHTML = data.length;

});




// Allocation Count

fetch("/api/allocations", {

headers:{
Authorization:getAuth()
}

})
.then(res => res.json())
.then(data => {

document.getElementById(
"allocationCount"
).innerHTML = data.length;

});




// Maintenance Count

fetch("/api/maintenance", {

headers:{
Authorization:getAuth()
}

})
.then(res => res.json())
.then(data => {

document.getElementById(
"maintenanceCount"
).innerHTML = data.length;

});





function logout(){

localStorage.clear();

window.location.href="../index.html";

}