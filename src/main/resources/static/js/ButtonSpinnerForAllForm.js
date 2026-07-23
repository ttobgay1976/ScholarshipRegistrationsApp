document.addEventListener("DOMContentLoaded", function () {

    document.querySelectorAll("form").forEach(function(form) {

        form.addEventListener("submit", function() {

            // Prevent duplicate submission
            if (form.dataset.submitted === "true") {
                event.preventDefault();
                return;
            }

            form.dataset.submitted = "true";

            const buttons = form.querySelectorAll(
                'button[type="submit"], input[type="submit"]' 
            );

            buttons.forEach(function(btn) {

                btn.disabled = true;

                if (btn.tagName === "BUTTON") {

                    btn.dataset.originalHtml = btn.innerHTML;

                    btn.innerHTML = `
                        <span class="spinner-border spinner-border-sm me-2"
                              role="status"
                              aria-hidden="true"></span>
                        Processing...
                    `;

                } else {

                    btn.dataset.originalValue = btn.value;
                    btn.value = "Processing...";
                }

            });

        });

    });

});