let wheel = document.getElementById("wheel");
let spinning = false;
let currentRotation = 0;
let selectedRecipeId = null;

// 구분선 생성 함수
function createWheelLines() {
    const wheelLines = document.getElementById("wheel-lines");
    if (!wheelLines) return;

    const slices = document.querySelectorAll(".slice");
    const count = slices.length;
    const lineCount = Math.min(count, 6); // 최대 6개
    const angleDiff = 360 / lineCount;

    wheelLines.innerHTML = '';

    for (let i = 0; i < lineCount; i++) {
        const line = document.createElement('div');
        line.className = 'wheel-line';
        line.style.transform = `rotate(${i * angleDiff}deg)`;
        wheelLines.appendChild(line);
    }
}

// 페이지 로드 시 구분선 생성
document.addEventListener("DOMContentLoaded", () => {
    createWheelLines();
});

// 룰렛 돌리기
function spin() {
    if (spinning) return;
    spinning = true;

    const slices = document.querySelectorAll(".slice");
    const count = slices.length;
    const sliceDeg = 360 / count;

    const targetIndex = Math.floor(Math.random() * count);
    const targetRotation = 360 * 5 + (360 - sliceDeg * targetIndex - sliceDeg / 2);

    currentRotation = targetRotation;
    wheel.style.transform = `rotate(${currentRotation}deg)`;

    setTimeout(() => {
        showResult(targetIndex);
        spinning = false;
    }, 4000);
}

// 결과 모달 표시
function showResult(index) {
    const slices = document.querySelectorAll(".slice");
    const slice = slices[index];
    const title = slice.querySelector(".recipe-title").innerText;
    const img = slice.querySelector("img").src;

    selectedRecipeId = slice.getAttribute("data-recipe-id") || index;

    document.getElementById("modalTitle").innerText = title;
    document.getElementById("modalImg").src = img;
    document.getElementById("modal").style.display = "flex";
}

function closeModal() {
    document.getElementById("modal").style.display = "none";
}

function openDetail() {
    if (selectedRecipeId) {
        selectDetail(selectedRecipeId);
    }
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

// 상단 탭 클릭 시 이동
function handleSegmentClick(button, url) {
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

// 로그아웃
function confirmLogout(){
    if(confirm("정말 로그아웃 하시겠습니까 ?")){
        window.location.href = "/logout";
    }
}
