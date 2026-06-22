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

/* =========================
   LOAD MY REQUESTS
   ========================= */
function loadRequests() {

    let employeeId = localStorage.getItem("employeeId");

    fetch("/api/maintenance/employee/" + employeeId, {
        headers: {
            Authorization: getAuth()
        }
    })
    .then(res => res.json())
    .then(data => {

        console.log("MY REQUESTS RESPONSE:", data);

        let list = Array.isArray(data) ? data : [];

        let rows = "";

        list.forEach(r => {

            rows += `
                <tr>
                    <td>${r.requestId}</td>

                    <!-- FIX: show assetName OR assetId -->
                    <td>${r.assetName ? r.assetName : r.assetId}</td>

                    <td>${r.description}</td>
                    <td>${r.status}</td>

                    <td>
                        ${r.createdAt 
                            ? new Date(r.createdAt).toLocaleString() 
                            : "-"}
                    </td>
                </tr>
            `;
        });

        document.querySelector("#reqTable tbody").innerHTML = rows;

        // if empty
        if (list.length === 0) {
            document.querySelector("#reqTable tbody").innerHTML =
                `<tr><td colspan="5">No Requests Found</td></tr>`;
        }
    })
    .catch(err => {
        console.error("Error loading requests:", err);
    });
}

/* =========================
   CREATE REQUEST
   ========================= */
function createRequest() {

    let assetId = document.getElementById("assetId").value;
    let description = document.getElementById("description").value;

    fetch("/api/maintenance", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: getAuth()
        },
        body: JSON.stringify({
            assetId: assetId,
            description: description
        })
    })
    .then(async res => {
        if (!res.ok) {
            let err = await res.text();
            throw new Error(err);
        }
        return res.json();
    })
    .then(() => {
        showMessage("Request Created Successfully!", "success");
        loadRequests();
    })
    .catch(err => {
        showMessage("Error: " + err.message, "error");
    });
}

/* =========================
   LOGOUT
   ========================= */
function logout() {
    localStorage.clear();
    window.location.href = "../index.html";
}

window.onload = loadRequests;
