

// Tab toggle + small UX niceties
window.addEventListener('DOMContentLoaded', function(){
    const msg = document.querySelector('.msg')?.value;
    if (msg) {
        alert(msg);
    }


    const userIdInput = document.getElementById("login-userId");
    const saveEmailCheckbox = document.getElementById("save-email");
    const autoLoginCheckbox = document.getElementById("auto-login");
    const form = document.getElementById("panel-login");

    // 1) 저장된 아이디가 있으면 불러오기
    const savedUserId = localStorage.getItem("savedUserId");
    if (savedUserId) {

        userIdInput.value = savedUserId;
        saveEmailCheckbox.checked = true;
    }

    // 2) 체크 해제하면 아이디 삭제
    saveEmailCheckbox.addEventListener("change", () => {
        if (saveEmailCheckbox.checked) {
            localStorage.setItem("savedUserId", userIdInput.value);
        } else {
            localStorage.removeItem("savedUserId");
        }
    });

    // 입력할 때도 자동 저장
    userIdInput.addEventListener("input", () => {
        if (saveEmailCheckbox.checked) {
            localStorage.setItem("savedUserId", userIdInput.value);
        }
    });

});

(function () {
    const $ = (sel) => document.querySelector(sel);
    const $$ = (sel) => Array.from(document.querySelectorAll(sel));

    const tabLogin = $('#tab-login');
    const tabSignup = $('#tab-signup');
    const panelLogin = $('#panel-login');
    const panelSignup = $('#panel-signup');

    function activate(tab) {
        const map = {
            'tab-login': panelLogin,
            'tab-signup': panelSignup,
        };
        $$('.tab').forEach((b) => b.classList.remove('active'));
        $$('.panel').forEach((p) => p.classList.remove('show'));
        tab.classList.add('active');
        map[tab.id].classList.add('show');
    }

    tabLogin.addEventListener('click', () => activate(tabLogin));
    tabSignup.addEventListener('click', () => activate(tabSignup));

    // Persist email when the checkbox is used
    const saveEmail = $('#save-email');
    const loginEmail = $('#login-email');

    // Load saved email if present
    try {
        const saved = localStorage.getItem('dishcovery.email');
        if (saved) {
            loginEmail.value = saved;
            if (saveEmail) saveEmail.checked = true;
        }
    } catch (_) {}

    saveEmail?.addEventListener('change', () => {
        try {
            if (saveEmail.checked) {
                localStorage.setItem('dishcovery.email', loginEmail.value.trim());
            } else {
                localStorage.removeItem('dishcovery.email');
            }
        } catch (_) {}
    });

    loginEmail?.addEventListener('blur', () => {
        if (saveEmail?.checked) {
            try { localStorage.setItem('dishcovery.email', loginEmail.value.trim()); } catch (_) {}
        }
    });

    // // Prevent real submission for demo; show simple alerts
    // panelLogin.addEventListener('submit', (e) => {
    //     e.preventDefault();
    //     const email = loginEmail.value.trim();
    //     const pw = document.getElementById('login-password').value;
    //     if (!email || !pw) return alert('이메일과 비밀번호를 입력해 주세요.');
    //     alert('로그인 시도: ' + email);
    // });


})();
