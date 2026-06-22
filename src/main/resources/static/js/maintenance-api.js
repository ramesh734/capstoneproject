let editId = null;

/* AUTH */
function getAuth(){
    let u = localStorage.getItem("username");
    let p = localStorage.getItem("password");
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

/* LOAD TABLE */
function loadMaintenance(){

fetch("/api/maintenance", {
headers: { Authorization: getAuth() }
})
.then(res => res.json())
.then(data => {

let list = Array.isArray(data) ? data : (data.data || []);

let table = "";

if(list.length === 0){
table = `<tr><td colspan="6">No Records Found</td></tr>`;
}
else{

list.forEach(m => {

table += `
<tr>
<td>${m.requestId}</td>
<td>${m.assetId}</td>
<td>${m.description}</td>
<td>${m.status}</td>
<td>${m.createdAt}</td>

<td>
<button onclick="editMaintenance(${m.requestId})">Edit</button>
<button onclick="deleteMaintenance(${m.requestId})">Delete</button>
</td>

</tr>
`;

});

}

document.querySelector("#maintTable tbody").innerHTML = table;

});

}

window.onload = loadMaintenance;

/* OPEN FORM */
function openForm(){
document.getElementById("maintForm").style.display = "block";
let mm = document.getElementById("modalMessage");
if (mm) { mm.style.display = "none"; mm.textContent = ""; }
}

/* CLOSE FORM */
function closeForm(){
document.getElementById("maintForm").style.display = "none";
clearForm();
}

/* CLEAR */
function clearForm(){
editId = null;

document.getElementById("assetId").value = "";
document.getElementById("description").value = "";
document.getElementById("status").value = "OPEN";
document.getElementById("createdAt").value = "";

document.getElementById("formTitle").innerText = "Add Maintenance";
}

/* SAVE + UPDATE */
function saveMaintenance(){

let maintenance = {
assetId: Number(document.getElementById("assetId").value),
description: document.getElementById("description").value,
status: document.getElementById("status").value,
createdAt: document.getElementById("createdAt").value
};

let url = "/api/maintenance";
let method = "POST";

if(editId != null){
url = "/api/maintenance/" + editId;
method = "PUT";
}

fetch(url, {
method: method,
headers: {
"Content-Type": "application/json",
Authorization: getAuth()
},
body: JSON.stringify(maintenance)
})
.then(async res => {

if(!res.ok){
let contentType = res.headers.get("Content-Type") || "";
if (contentType.includes("json")) {
let errJson = await res.json();
let msgs = Object.values(errJson);
throw new Error(msgs.join(" | "));
} else {
let err = await res.text();
throw new Error(err);
}
}

return res.json();
})
.then(() => {
showMessage(editId ? "Maintenance Updated Successfully" : "Maintenance Added Successfully", "success");
closeForm();
loadMaintenance();
})
.catch(err => {
console.error(err);
showModalMessage(err.message, "error");
});

}

/* EDIT */
function editMaintenance(id){

fetch("/api/maintenance/" + id, {
headers: { Authorization: getAuth() }
})
.then(res => res.json())
.then(m => {

editId = m.requestId;

document.getElementById("assetId").value = m.assetId;
document.getElementById("description").value = m.description;
document.getElementById("status").value = m.status;
document.getElementById("createdAt").value = m.createdAt;

openForm();

});

}

/* DELETE */
function deleteMaintenance(id){

if (!confirm("Are you sure you want to delete this maintenance request?")) return;

fetch("/api/maintenance/" + id, {
method: "DELETE",
headers: { Authorization: getAuth() }
})
.then(async res => {
if (!res.ok) {
let err = await res.text();
throw new Error(err);
}
showMessage("Maintenance request deleted successfully", "success");
loadMaintenance();
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
