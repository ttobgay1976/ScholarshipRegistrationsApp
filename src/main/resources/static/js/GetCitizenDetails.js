
//THIS IS FOR TE CITIZEN API CALL JS

function fetchCitizenDetails(cid) {

    let msgBox = document.getElementById("cidMessage");

    // 1. Validate CID
    if (!cid || cid.trim() === "") {
        msgBox.style.color = "red";
        msgBox.innerText = "CID is required";
        return;
    }

    msgBox.style.color = "blue";
    msgBox.innerText = "Fetching citizen details...";

   fetch('/service/citizen/details?cid=' + cid)
    .then(async res => {

        const text = await res.text();

        console.log("RAW RESPONSE:", text);

        // ❌ if HTML returned
        if (text.trim().startsWith("<!DOCTYPE") || text.includes("<html")) {
            throw new Error("DCRC service returned HTML error page");
        }

        let data;
        try {
            data = JSON.parse(text);
        } catch (e) {
            throw new Error("Invalid JSON from server");
        }

        return data;
    })
    .then(data => {

        if (!data) {
            throw new Error("Empty response from DCRC service");
        }

        if (data.error) {
            throw new Error(data.error);
        }

        msgBox.style.color = "green";
        msgBox.innerText = "Citizen details loaded successfully";

        const setVal = (id, value) => {
            const el = document.getElementById(id);
            if (el) el.value = value || "";
        };

        setVal("firstName", data.firstName);
        setVal("middleName", data.middleName);
        setVal("lastName", data.lastName);
        setVal("dateOfBirth", data.dob || data.dateOfBirth);
        setVal("gender", data.gender);

        setVal("fatherName", data.fatherName);
        setVal("motherName", data.motherName);
        setVal("dzongkhagName", data.dzongkhagName);
        setVal("gewogName", data.gewogName);
        setVal("villageName", data.villageName);

	  setVal("id", data.id || "");
        setVal("dzongkhagId", data.dzongkhagId || "");
        setVal("gewogId", data.gewogId || "");

    })
    .catch(err => {

        console.error("DEBUG ERROR:", err);

        msgBox.style.color = "red";
        msgBox.innerText = err.message;
    });
  }