let editId = null;

/* AUTH */
function getAuth(){
    let u = localStorage.getItem("username");
    let p = localStorage.getItem("password");

    if(!u || !p){
        console.error("No auth found in localStorage");
        return "";
    }

    return "Basic " + btoa(u + ":" + p);
}

function showMessage(text, type) {
    let el = document.getElementById("message");
    if (!el) return;
    el.textContent = text;
    el.className = "message " + (type || "error");
    el.style.display = "block";
    setTimeout(() => { el.style.display = "none"; }, 5000);
}

function showModalMessage(text, type) {
    let el = document.getElementById("modalMessage");
    if (!el) return;
    el.textContent = text;
    el.className = "message " + (type || "error");
    el.style.display = "block";
}

/* =========================
   LOAD VENDORS
   ========================= */
function loadVendors(){

console.log("Loading vendors...");

fetch("/api/vendors", {
method: "GET",
headers: {
"Content-Type": "application/json",
Authorization: getAuth()
}
})
.then(async res => {

if(!res.ok){
let errorText = await res.text();
throw new Error("HTTP " + res.status + " - " + errorText);
}

return res.json();
})
.then(data => {

console.log("Vendor API Response:", data);

// handle array or object response safely
let list = Array.isArray(data) ? data : (data.data || []);

let table = "";

if(list.length === 0){
table = `
<tr>
<td colspan="6" style="text-align:center;">No Vendors Found</td>
</tr>
`;
}
else{
list.forEach(v => {

table += `
<tr>
<td>${v.vendorId}</td>
<td>${v.vendorName}</td>
<td>${v.email}</td>
<td>${v.contactNumber}</td>
<td>${v.address}</td>

<td>
<button onclick="editVendor(${v.vendorId})">Edit</button>
<button onclick="deleteVendor(${v.vendorId})">Delete</button>
</td>

</tr>
`;

});
}

// IMPORTANT: correct table id
document.querySelector("#vendorTable tbody").innerHTML = table;

})
.catch(err => {

console.error("Vendor Load Error:", err);

document.querySelector("#vendorTable tbody").innerHTML = `
<tr>
<td colspan="6" style="color:red;text-align:center;">
Failed to load vendors
</td>
</tr>
`;

});

}

/* AUTO LOAD */
window.onload = loadVendors;

/* OPEN FORM */
function openForm(){
document.getElementById("vendorForm").style.display = "block";
let mm = document.getElementById("modalMessage");
if (mm) { mm.style.display = "none"; mm.textContent = ""; }
}

/* CLOSE FORM */
function closeForm(){
document.getElementById("vendorForm").style.display = "none";
clearForm();
}

/* CLEAR FORM */
function clearForm(){
editId = null;

document.getElementById("name").value = "";
document.getElementById("email").value = "";
document.getElementById("phone").value = "";
document.getElementById("address").value = "";

document.getElementById("formTitle").innerText = "Add Vendor";
}

/* SAVE + UPDATE */
function saveVendor(){

let vendor = {
vendorName: document.getElementById("name").value,
email: document.getElementById("email").value,
contactNumber: document.getElementById("phone").value,
address: document.getElementById("address").value
};

let url = "/api/vendors";
let method = "POST";

if(editId != null){
url = "/api/vendors/" + editId;
method = "PUT";
}

fetch(url, {
method: method,
headers: {
"Content-Type": "application/json",
Authorization: getAuth()
},
body: JSON.stringify(vendor)
})
.then(async res => {

if(!res.ok){
let contentType = res.headers.get("Content-Type") || "";
if (contentType.includes("json")) {
let errJson = await res.json();
let msgs = Object.values(errJson);
throw new Error(msgs.join(" | "));
} else {
let text = await res.text();
throw new Error(text);
}
}

return res.json();
})
.then(() => {

showMessage(editId ? "Vendor Updated Successfully" : "Vendor Added Successfully", "success");
closeForm();
loadVendors();

})
.catch(err => {
console.error("Save Vendor Error:", err);
showModalMessage(err.message, "error");
});

}

/* EDIT */
function editVendor(id){

fetch("/api/vendors/" + id, {
headers: { Authorization: getAuth() }
})
.then(res => res.json())
.then(v => {

editId = id;

document.getElementById("name").value = v.vendorName;
document.getElementById("email").value = v.email;
document.getElementById("phone").value = v.contactNumber;
document.getElementById("address").value = v.address;

document.getElementById("formTitle").innerText = "Update Vendor";

openForm();

});

}

/* DELETE */
function deleteVendor(id){

if (!confirm("Are you sure you want to delete this vendor?")) return;

fetch("/api/vendors/" + id, {
method: "DELETE",
headers: { Authorization: getAuth() }
})
.then(async res => {
if (!res.ok) {
let err = await res.text();
throw new Error(err);
}
showMessage("Vendor deleted successfully", "success");
loadVendors();
})
.catch(err => {
showMessage("Error: " + err.message, "error");
});

}

/* LOGOUT */
function logout(){
localStorage.clear();
window.location.href="../index.html";
}
