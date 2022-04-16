window.addEventListener("DOMContentLoaded",(event) => {
    const togglePassword = document.getElementsByClassName("bi");
    for (let i = 0; i < togglePassword.length; i++) {
        let elem = togglePassword.item(i);
        elem.onclick = function () {
            const recordId = elem.dataset.iconid;
            const passwordTd = document.getElementById(recordId);
            const isVisible = passwordTd.dataset.vis;
            const pass = passwordTd.dataset.pass;
            if(isVisible === "1") {
                elem.classList.add("bi-eye-slash");
                elem.classList.remove("bi-eye");
                passwordTd.innerText = pass;

                passwordTd.dataset.vis = "0";
            }else {
                elem.classList.add("bi-eye");
                elem.classList.remove("bi-eye-slash");
                passwordTd.innerText = "****";
                passwordTd.dataset.vis = "1";
            }


        }
    }
});