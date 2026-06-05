
// sidebar.js
document.addEventListener('DOMContentLoaded', function () {
    const path = window.location.pathname;
    document.querySelectorAll('.child-menu').forEach(function(link) {
        if(link.getAttribute('href') === path) {
            link.classList.add('active');
            const parentCollapse = link.closest('.submenu');
            if(parentCollapse) {
                parentCollapse.classList.add('show');
                const parentLink = parentCollapse.previousElementSibling;
                if(parentLink && parentLink.classList.contains('parent-menu')) {
                    parentLink.classList.add('active');
                }
            }
        }
    });
});