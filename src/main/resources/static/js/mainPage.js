// Segmented control toggle
document.addEventListener('click', (e) => {
    const segBtn = e.target.closest('.seg-btn');
    if (segBtn) {
        const wrap = segBtn.parentElement;
        wrap.querySelectorAll('.seg-btn').forEach(b => b.classList.remove('active'));
        segBtn.classList.add('active');
    }

    const sortBtn = e.target.closest('.sort');
    if (sortBtn) {
        const wrap = sortBtn.parentElement;
        wrap.querySelectorAll('.sort').forEach(b => b.classList.remove('active'));
        sortBtn.classList.add('active');
    }
});

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

function selectDetail(recipeId){

    alert(recipeId);

}