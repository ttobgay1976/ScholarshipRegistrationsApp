
//THIS IS FOR TE CITIZEN API CALL JS

function fetchCitizenDetails(cid) {

    let msgBox = document.getElementById("cidMessage");

    // 1. Validate CID
	if (!cid || !/^\d{11}$/.test(cid.trim())) {
	
	    Swal.fire({
	        icon: 'warning',
	        title: 'Invalid Citizen ID',
	        text: 'Please enter a valid 11-digit Citizen ID.',
	        confirmButtonText: 'OK'
	    });
	
	    return;
	}

    //msgBox.style.color = "blue";
    //msgBox.innerText = "Fetching citizen details...";
	Swal.fire({
	    icon: 'info',
	    title: 'Fetching DCRC Records...',
	    text: 'Please wait...'
	});

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

       // msgBox.style.color = "green";
       // msgBox.innerText = "Citizen details loaded successfully";
        Swal.fire({
		    icon: 'success',
		    title: 'Citizen Details',
		    text: 'Your Citizen details are fetched successfully from DCRC API',
		    confirmButtonColor: '#198754',
		    confirmButtonText: 'OK',
		    allowOutsideClick: false
		});

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

       // msgBox.style.color = "red";
        //msgBox.innerText = err.message;
        
        Swal.fire({
		    icon: 'error',
		    title: 'Invalid Citizen ID',
		    text: err.message,
		    confirmButtonColor: '#dc3545'
		});
    });
  }