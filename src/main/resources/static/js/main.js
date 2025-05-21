// Main JavaScript file for Car Rental Agency

// Document ready function
document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function(popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Add active class to current navigation item
    const currentLocation = window.location.pathname;
    const navLinks = document.querySelectorAll('.navbar-nav .nav-link');
    
    navLinks.forEach(link => {
        const linkPath = link.getAttribute('href');
        if (linkPath && currentLocation.includes(linkPath) && linkPath !== '/web') {
            link.classList.add('active');
        } else if (currentLocation === '/web' && linkPath === '/web') {
            link.classList.add('active');
        }
    });

    // Date validation for rental forms
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    
    if (startDateInput && endDateInput) {
        startDateInput.addEventListener('change', validateDates);
        endDateInput.addEventListener('change', validateDates);
    }

    function validateDates() {
        const startDate = new Date(startDateInput.value);
        const endDate = new Date(endDateInput.value);
        
        if (endDate <= startDate) {
            alert('End date must be after start date.');
            endDateInput.value = '';
        }
    }    // Form validation
    const forms = document.querySelectorAll('.needs-validation');
    
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            
            form.classList.add('was-validated');
        }, false);
    });
    
    // Logout functionality
    const logoutLink = document.getElementById('logout-link');
    if (logoutLink) {
        logoutLink.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Get JWT token from local storage
            const token = localStorage.getItem('jwt_token');
            
            // Call the logout API
            fetch('/api/auth/logout', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            })
            .then(response => {
                // Remove token from local storage
                localStorage.removeItem('jwt_token');
                
                // Redirect to login page
                window.location.href = '/web/login?logout';
            })
            .catch(error => {
                console.error('Logout failed:', error);
                // Redirect anyway
                window.location.href = '/web/login?logout';
            });
        });
    }
});
