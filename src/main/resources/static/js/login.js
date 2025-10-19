// Tab toggle + small UX niceties
(function () {
    const $ = (sel) => document.querySelector(sel);
    const $$ = (sel) => Array.from(document.querySelectorAll(sel));

    const tabLogin = $('#tab-login');
    const tabSignup = $('#tab-signup');
    const panelLogin = $('#panel-login');
    const panelSignup = $('#panel-signup');

    function activate(tab) {
        alert("test");
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

    // Prevent real submission for demo; show simple alerts
    panelLogin.addEventListener('submit', (e) => {
        e.preventDefault();
        const email = loginEmail.value.trim();
        const pw = document.getElementById('login-password').value;
        if (!email || !pw) return alert('이메일과 비밀번호를 입력해 주세요.');
        alert('로그인 시도: ' + email);
    });

    panelSignup.addEventListener('submit', (e) => {
        e.preventDefault();
        const e1 = document.getElementById('signup-email').value.trim();
        const p1 = document.getElementById('signup-password').value;
        const p2 = document.getElementById('signup-password2').value;
        if (!e1 || !p1 || !p2) return alert('모든 항목을 입력해 주세요.');
        if (p1 !== p2) return alert('비밀번호가 일치하지 않습니다.');
        alert('회원가입 시도: ' + e1);
    });
})();