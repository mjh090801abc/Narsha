let wheel = document.getElementById("wheel");
let spinning = false;
let selectedRecipeId = null;

/* 구분선 생성 */
function createWheelLines() {
    const container = document.getElementById("wheel-lines");
    if (!container) return;

    const slices = document.querySelectorAll(".slice");
    const count = slices.length;
    const angle = 360 / count;

    container.innerHTML = "";

    for (let i = 0; i < count; i++) {
        const line = document.createElement("div");
        line.className = "wheel-line";
        line.style.transform = `rotate(${i * angle}deg)`;
        container.appendChild(line);
    }
}

document.addEventListener("DOMContentLoaded", createWheelLines);

/* 룰렛 회전 */
function spin() {
    if (spinning) return;
    spinning = true;

    const slices = document.querySelectorAll(".slice");
    const count = slices.length;
    const sliceDeg = 360 / count;

    const targetIndex = Math.floor(Math.random() * count);
    const rotation = 360 * 5 + (360 - sliceDeg * targetIndex - sliceDeg / 2);

    wheel.style.transform = `rotate(${rotation}deg)`;

    setTimeout(() => {
        showResult(targetIndex);
        spinning = false;
    }, 4000);
}

/* 결과 */
function showResult(index) {
    const slice = document.querySelectorAll(".slice")[index];
    selectedRecipeId = slice.dataset.recipeId;

    document.getElementById("modalTitle").innerText =
        slice.querySelector(".recipe-title").innerText;
    document.getElementById("modalImg").src =
        slice.querySelector("img").src;

    document.getElementById("modal").style.display = "flex";
}

function closeModal() {
    document.getElementById("modal").style.display = "none";
}

function openDetail() {
    if (selectedRecipeId) selectDetail(selectedRecipeId);
}

/* 상세 이동 */
function selectDetail(recipeId) {
    const form = document.createElement("form");
    form.method = "GET";
    form.action = "/recipe/detail";

    const input = document.createElement("input");
    input.type = "hidden";
    input.name = "recipeId";
    input.value = recipeId;

    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
}

/* 상단 탭 */
function handleSegmentClick(button, url) {
    setTimeout(() => location.href = url, 300);
}

/* 로그아웃 */
function confirmLogout() {
    if (confirm("정말 로그아웃 하시겠습니까 ?")) {
        location.href = "/logout";
    }
}