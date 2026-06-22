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

/* MIN DATE = TODAY (NO PAST DATES) */
function setMinDates() {
    let today = new Date().toISOString().split("T")[0];

    document.getElementById("allocatedDate").setAttribute("min", today);
    document.getElementById("returnDate").setAttribute("min", today);
}

/* LOAD TABLE */
function loadAllocations() {

    fetch("/api/allocations", {
        headers: { Authorization: getAuth() }
    })
        .then(res => res.json())
        .then(data => {

            let list = Array.isArray(data) ? data : (data.data || []);

            let table = "";

            if (list.length === 0) {
                table = `<tr><td colspan="8">No Allocations Found</td></tr>`;
            }
            else {

                list.forEach(a => {

                    table += `
<tr>
<td>${a.allocationId}</td>
<td>${a.assetId}</td>
<td>${a.employeeId}</td>
<td>${a.allocatedDate}</td>
<td>${a.returnDate || "		Yet To Be Returned	"}</td>
<td>${a.status}</td>
<td>${a.remarks}</td>

<td>
<button onclick="editAllocation(${a.allocationId})">Edit</button>
<button onclick="deleteAllocation(${a.allocationId})">Delete</button>
</td>

</tr>
`;

                });

            }

            document.querySelector("#allocTable tbody").innerHTML = table;

        });

}

window.onload = function() {
    setMinDates();
    loadAllocations();
};

/* OPEN */
function openForm() {
    document.getElementById("allocForm").style.display = "block";
    setMinDates();
    let mm = document.getElementById("modalMessage");
    if (mm) { mm.style.display = "none"; mm.textContent = ""; }
}

/* CLOSE */
function closeForm() {
    document.getElementById("allocForm").style.display = "none";
    clearForm();
}

/* CLEAR */
function clearForm() {
    editId = null;

    document.getElementById("assetId").value = "";
    document.getElementById("employeeId").value = "";
    document.getElementById("allocatedDate").value = "";
    document.getElementById("returnDate").value = "";
    document.getElementById("remarks").value = "";

    document.getElementById("status").value = "ASSIGNED";
    document.getElementById("formTitle").innerText = "Add Allocation";
}

/* SAVE */
function saveAllocation() {

    let allocation = {
        assetId: document.getElementById("assetId").value,
        employeeId: document.getElementById("employeeId").value,
        allocatedDate: document.getElementById("allocatedDate").value,
        returnDate: document.getElementById("returnDate").value,
        status: document.getElementById("status").value,
        remarks: document.getElementById("remarks").value
    };

    let url = "/api/allocations";
    let method = "POST";

    if (editId != null) {
        url = "/api/allocations/" + editId;
        method = "PUT";
    }

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            Authorization: getAuth()
        },
        body: JSON.stringify(allocation)
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
            showMessage(editId ? "Allocation Updated Successfully" : "Allocation Added Successfully", "success");
            closeForm();
            loadAllocations();
        })
        .catch(err => {
            showModalMessage(err.message, "error");
        });

}

/* EDIT */
function editAllocation(id) {

    fetch("/api/allocations/" + id, { headers: { Authorization: getAuth() } })
        .then(res => res.json())
        .then(a => {

            editId = id;

            document.getElementById("assetId").value = a.assetId;
            document.getElementById("employeeId").value = a.employeeId;
            document.getElementById("allocatedDate").value = a.allocatedDate;
            document.getElementById("returnDate").value = a.returnDate;
            document.getElementById("remarks").value = a.remarks;
            document.getElementById("status").value = a.status;

            openForm();

        });

}

/* DELETE */
function deleteAllocation(id) {

    if (!confirm("Are you sure you want to delete this allocation?")) return;

    fetch("/api/allocations/" + id, {
        method: "DELETE",
        headers: { Authorization: getAuth() }
    })
        .then(async res => {
            if (!res.ok) {
                let err = await res.text();
                throw new Error(err);
            }
            showMessage("Allocation deleted successfully", "success");
            loadAllocations();
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
