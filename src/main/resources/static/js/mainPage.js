// Segmented control toggle
/*
document.addEventListener('click', (e) => {

    const sortBtn = e.target.closest('.sort');
    if (sortBtn) {
        const wrap = sortBtn.parentElement;
        wrap.querySelectorAll('.sort').forEach(b => b.classList.remove('active'));
        sortBtn.classList.add('active');
    }




});
*/
// Submit search on button click or Enter
const searchWrapClass = document.querySelector('.search-wrap');
if (searchWrapClass) {
    const input = searchWrapClass.querySelector('input');
    const btn = searchWrapClass.querySelector('.icon-btn');
    const submit = () => {
        const q = input.value.trim();
        if (!q) return;
        // For now, just log. Wire to real search later.
        console.log('검색:', q);
    };
    btn?.addEventListener('click', submit);
    input?.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') submit();
    });
}

function selectDetail(recipeId) {
    const form = document.createElement("form");
    form.method = "GET";
    form.action = "/recipe/detail";
    form.target = "_self";

    const hidden = document.createElement("input");
    hidden.type = "hidden";
    hidden.name = "recipeId";
    hidden.value = recipeId;

    form.appendChild(hidden);
    document.body.appendChild(form);
    form.submit();
}

function handleSegmentClick(button, url) {
    // 활성화 클래스 토글
    const wrap = button.parentElement;
    wrap.querySelectorAll('button').forEach(b => {
        b.classList.remove('active');
        b.setAttribute('aria-selected', 'false');
    });
    button.classList.add('active');
    button.setAttribute('aria-selected', 'true');

    setTimeout(() => {
        window.location.href = url;
    }, 300);

}

function confirmLogout(){
    if(confirm("정말 로그아웃 하시겠습니까 ?")){
        window.location.href = "/logout";
    }
}