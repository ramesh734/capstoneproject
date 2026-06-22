let editId = null;

/* AUTH */
function getAuth() {
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

/* =========================
   LOAD DROPDOWNS
   ========================= */
function loadDropdowns() {

    // categories
    fetch("/api/categories", { headers: { Authorization: getAuth() } })
        .then(res => res.json())
        .then(data => {
            let list = Array.isArray(data) ? data : (data.data || []);
            let html = "<option value=''>Select Category</option>";
            list.forEach(c => {
                html += `<option value="${c.categoryId}">${c.categoryName}</option>`;
            });
            document.getElementById("categoryId").innerHTML = html;
        });

    // vendors
    fetch("/api/vendors", { headers: { Authorization: getAuth() } })
        .then(res => res.json())
        .then(data => {
            let list = Array.isArray(data) ? data : (data.data || []);
            let html = "<option value=''>Select Vendor</option>";
            list.forEach(v => {
                html += `<option value="${v.vendorId}">${v.vendorName}</option>`;
            });
            document.getElementById("vendorId").innerHTML = html;
        });

}

/* LOAD ASSETS */
function loadAssets() {

    fetch("/api/assets", { headers: { Authorization: getAuth() } })
        .then(res => res.json())
        .then(data => {

            let list = Array.isArray(data) ? data : (data.data || []);

            let table = "";

            if (list.length === 0) {
                table = `<tr><td colspan="7">No Assets Found</td></tr>`;
            }
            else {

                list.forEach(a => {

                    table += `
<tr>
<td>${a.assetId}</td>
<td>${a.assetName}</td>
<td>${a.serialNumber}</td>
<td>${a.availability}</td>
<td>${a.categoryName || a.category?.categoryName || ""}</td>
<td>${a.vendorName || a.vendor?.vendorName || ""}</td>

<td>
<button onclick="editAsset(${a.assetId})">Edit</button>
<button onclick="deleteAsset(${a.assetId})">Delete</button>
</td>

</tr>
`;

                });

            }

            document.querySelector("#assetTable tbody").innerHTML = table;

        });

}

window.onload = function() {
    loadDropdowns();
    loadAssets();
};

/* OPEN */
function openForm() {
    document.getElementById("assetForm").style.display = "block";
    let mm = document.getElementById("modalMessage");
    if (mm) { mm.style.display = "none"; mm.textContent = ""; }
}

/* CLOSE */
function closeForm() {
    document.getElementById("assetForm").style.display = "none";
    clearForm();
}

/* CLEAR */
function clearForm() {
    editId = null;

    document.getElementById("name").value = "";
    document.getElementById("serialNumber").value = "";
    document.getElementById("availability").value = "AVAILABLE";
    document.getElementById("formTitle").innerText = "Add Asset";
}

/* SAVE */
function saveAsset() {

    let asset = {
        assetName: document.getElementById("name").value,
        serialNumber: document.getElementById("serialNumber").value,
        availability: document.getElementById("availability").value,
        categoryId: document.getElementById("categoryId").value,
        vendorId: document.getElementById
            ("vendorId").value
    };

    let url = "/api/assets";
    let method = "POST";

    if (editId != null) {
        url = "/api/assets/" + editId;
        method = "PUT";
    }

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            Authorization: getAuth()
        },
        body: JSON.stringify(asset)
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
            showMessage(editId ? "Asset Updated Successfully" : "Asset Added Successfully", "success");
            closeForm();
            loadAssets();
        })
        .catch(err => {
            showModalMessage(err.message, "error");
        });

}

/* EDIT */
function editAsset(id) {

    fetch("/api/assets/" + id, { headers: { Authorization: getAuth() } })
        .then(res => res.json())
        .then(a => {

            editId = id;

            document.getElementById("name").value = a.assetName;
            document.getElementById("serialNumber").value = a.serialNumber;
            document.getElementById("availability").value = a.availability;

            openForm();

        });

}

/* DELETE */
function deleteAsset(id) {

    if (!confirm("Are you sure you want to delete this asset?")) return;

    fetch("/api/assets/" + id, {
        method: "DELETE",
        headers: { Authorization: getAuth() }
    })
        .then(async res => {
            if (!res.ok) {
                let err = await res.text();
                throw new Error(err);
            }
            showMessage("Asset deleted successfully", "success");
            loadAssets();
        })
        .catch(err => {
            showMessage("Error: " + err.message, "error");
        });

}

/* LOGOUT */
function logout() {
    localStorage.clear();
    window.location.href = "../index.html";
}
