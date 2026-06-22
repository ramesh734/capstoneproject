function getAuth() {
    let u = localStorage.getItem("username");
    let p = localStorage.getItem("password");
    return "Basic " + btoa(u + ":" + p);
}

function loadAssets() {

    let table = document.getElementById("assetsTable");

    if (!table) {
        console.error("❌ assetsTable NOT FOUND - wrong page or HTML issue");
        return;
    }

    let tbody = table.getElementsByTagName("tbody")[0];

    if (!tbody) {
        console.error("❌ tbody NOT FOUND");
        return;
    }

    let employeeId = localStorage.getItem("employeeId");

    fetch("/api/allocations", {
        headers: {
            Authorization: getAuth()
        }
    })
    .then(res => res.json())
    .then(data => {

        let list = Array.isArray(data) ? data : [];

        let myAssets = list.filter(a =>
            String(a.employeeId) === String(employeeId)
        );

        let rows = "";

        myAssets.forEach(a => {
            rows += `
                <tr>
                    <td>${a.allocationId}</td>
                    <td>${a.assetId}</td>
                    <td>${a.assetName}</td>
                    <td>${a.status}</td>
                   <td>${a.returnDate ? a.returnDate : "Yet To Be Returned"}</td>
                </tr>
            `;
        });

        tbody.innerHTML = rows || `<tr><td colspan="5">No Assets Found</td></tr>`;
    })
    .catch(err => console.error("API ERROR:", err));
}

window.onload = loadAssets;

function logout() {
    localStorage.clear();
    window.location.href = "../index.html";
}