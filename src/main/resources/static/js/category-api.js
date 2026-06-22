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

/* LOAD CATEGORIES */
function loadCategories(){

fetch("/api/categories", {
headers:{ Authorization:getAuth() }
})
.then(res => res.json())
.then(data => {

let list = Array.isArray(data) ? data : (data.data || []);

let table = "";

if(list.length === 0){
table = `<tr><td colspan="3">No Categories Found</td></tr>`;
}
else{

list.forEach(c => {

table += `
<tr>
<td>${c.categoryId}</td>
<td>${c.categoryName}</td>

<td>
<button onclick="editCategory(${c.categoryId})">Edit</button>
<button onclick="deleteCategory(${c.categoryId})">Delete</button>
</td>

</tr>
`;

});

}

document.querySelector("#categoryTable tbody").innerHTML = table;

})
.catch(err => {

console.error("Category Load Error:", err);

document.querySelector("#categoryTable tbody").innerHTML =
`<tr><td colspan="3" style="color:red;">Failed to load</td></tr>`;

});

}

window.onload = loadCategories;

/* OPEN FORM */
function openForm(){
document.getElementById("categoryForm").style.display = "block";
let mm = document.getElementById("modalMessage");
if (mm) { mm.style.display = "none"; mm.textContent = ""; }
}

/* CLOSE FORM */
function closeForm(){
document.getElementById("categoryForm").style.display = "none";
clearForm();
}

/* CLEAR */
function clearForm(){
editId = null;
document.getElementById("name").value = "";
document.getElementById("formTitle").innerText = "Add Category";
}

/* SAVE + UPDATE */
function saveCategory(){

let category = {
categoryName: document.getElementById("name").value
};

let url = "/api/categories";
let method = "POST";

if(editId != null){
url = "/api/categories/" + editId;
method = "PUT";
}

fetch(url, {
method: method,
headers:{
"Content-Type":"application/json",
Authorization:getAuth()
},
body: JSON.stringify(category)
})
.then(async res => {
if (!res.ok) {
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

showMessage(editId ? "Category Updated Successfully" : "Category Added Successfully", "success");
closeForm();
loadCategories();

})
.catch(err => {
showModalMessage(err.message, "error");
});

}

/* EDIT */
function editCategory(id){

fetch("/api/categories/" + id, {
headers:{ Authorization:getAuth() }
})
.then(res => res.json())
.then(c => {

editId = id;

document.getElementById("name").value = c.categoryName;

document.getElementById("formTitle").innerText = "Update Category";

openForm();

});

}

/* DELETE */
function deleteCategory(id){

if (!confirm("Are you sure you want to delete this category?")) return;

fetch("/api/categories/" + id, {
method:"DELETE",
headers:{ Authorization:getAuth() }
})
.then(async res => {
if (!res.ok) {
let err = await res.text();
throw new Error(err);
}
showMessage("Category deleted successfully", "success");
loadCategories();
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
