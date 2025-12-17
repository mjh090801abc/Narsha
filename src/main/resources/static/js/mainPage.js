// ==========================================
// 전역 변수
// ==========================================
let wheel = document.getElementById("wheel");
let spinning = false;
let selectedRecipeId = null;
let currentRotation = 0; // 현재 회전 각도 추적

// ==========================================
// 서버에서 받은 레시피 중 최대 6개만 랜덤 선택
// ==========================================
function selectRandomRecipes() {
    const allSlices = document.querySelectorAll(".slice");
    const count = allSlices.length;

    if (count === 0) {
        console.warn('레시피가 없습니다.');
        return;
    }

    // 6개 이하면 그대로, 초과하면 6개만 랜덤 선택
    if (count <= 6) {
        console.log(`${count}개의 레시피를 모두 사용합니다.`);
        return;
    }

    console.log(`${count}개 중 6개를 랜덤으로 선택합니다.`);

    // 인덱스 배열 생성 후 섞기
    const indices = Array.from({length: count}, (_, i) => i);
    for (let i = indices.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [indices[i], indices[j]] = [indices[j], indices[i]];
    }

    // 선택되지 않은 레시피는 숨기기
    const selectedIndices = indices.slice(0, 6);
    allSlices.forEach((slice, i) => {
        if (!selectedIndices.includes(i)) {
            slice.style.display = 'none';
        }
    });
}

// ==========================================
// 페이지 로드 시 레시피 위치 설정
// ==========================================
function positionRecipes() {
    const slices = document.querySelectorAll(".slice");
    const visibleSlices = Array.from(slices).filter(s => s.style.display !== 'none');
    const count = visibleSlices.length;

    // 레시피가 없으면 리턴
    if (count === 0) {
        console.warn('레시피가 없습니다.');
        return;
    }

    const radius = 140; // 룰렛 안에 완전히 들어가도록 더 축소
    const angleStep = 360 / count;

    visibleSlices.forEach((slice, i) => {
        // 각도 계산 - 구분선 사이 중앙에 배치하기 위해 angleStep/2 만큼 오프셋 추가
        const angle = (i * angleStep + angleStep / 2 - 90) * (Math.PI / 180);

        // 원형 좌표 계산 (삼각함수 사용)
        const x = radius * Math.cos(angle);
        const y = radius * Math.sin(angle);

        // 위치 적용
        slice.style.transform = `translate(${x}px, ${y}px)`;
    });

    console.log(`${count}개의 레시피가 원형으로 배치되었습니다.`);
}

// ==========================================
// 구분선 생성
// ==========================================
function createWheelLines() {
    const container = document.getElementById("wheel-lines");
    if (!container) {
        console.error('wheel-lines 컨테이너를 찾을 수 없습니다.');
        return;
    }

    const slices = document.querySelectorAll(".slice");
    const visibleSlices = Array.from(slices).filter(s => s.style.display !== 'none');
    const count = visibleSlices.length;

    if (count === 0) {
        console.warn('레시피가 없어 구분선을 생성하지 않습니다.');
        return;
    }

    const angleStep = 360 / count;

    // 기존 구분선 제거
    container.innerHTML = "";

    // 레시피 개수만큼 구분선 생성
    for (let i = 0; i < count; i++) {
        const line = document.createElement("div");
        line.className = "wheel-line";
        line.style.transform = `rotate(${i * angleStep}deg)`;
        container.appendChild(line);
    }

    console.log(`${count}개의 구분선이 생성되었습니다.`);
}

// ==========================================
// 페이지 로드 완료 후 초기화
// ==========================================
document.addEventListener("DOMContentLoaded", function() {
    console.log('페이지 로드 완료 - 룰렛 초기화 시작');

    // 1단계: 최대 6개 레시피만 랜덤 선택
    selectRandomRecipes();

    // 2단계: 선택된 레시피 원형 배치
    positionRecipes();

    // 3단계: 구분선 생성
    createWheelLines();

    console.log('룰렛 초기화 완료');
});

// ==========================================
// 룰렛 회전 함수
// ==========================================
function spin() {
    if (spinning) {
        console.log('이미 회전 중입니다.');
        return;
    }

    const slices = document.querySelectorAll(".slice");
    const visibleSlices = Array.from(slices).filter(s => s.style.display !== 'none');
    const count = visibleSlices.length;

    if (count === 0) {
        alert('레시피가 없습니다.');
        return;
    }

    spinning = true;
    console.log('룰렛 회전 시작');

    const sliceDeg = 360 / count;

    // 랜덤으로 선택될 인덱스
    const targetIndex = Math.floor(Math.random() * count);
    console.log(`목표 인덱스: ${targetIndex}`);

    // 회전 각도 계산
    const spinRotations = 360 * 5; // 5바퀴 회전
    const targetAngle = targetIndex * sliceDeg;

    // 12시 방향(위쪽 화살표)에 정확히 맞추기 위한 각도 계산
    const finalRotation = currentRotation + spinRotations + (360 - targetAngle + sliceDeg / 2);

    console.log(`최종 회전 각도: ${finalRotation}도`);

    // 룰렛 회전 적용
    wheel.style.transform = `rotate(${finalRotation}deg)`;
    currentRotation = finalRotation % 360; // 다음 회전을 위해 현재 각도 저장

    // 회전 완료 후 결과 표시 (4초 후)
    setTimeout(() => {
        showResult(visibleSlices[targetIndex]);
        spinning = false;
        console.log('룰렛 회전 완료');
    }, 4000);
}

// ==========================================
// 결과 표시 (모달 열기)
// ==========================================
function showResult(slice) {
    if (!slice) {
        console.error('선택된 레시피를 찾을 수 없습니다.');
        return;
    }

    // 레시피 정보 추출
    selectedRecipeId = slice.dataset.recipeId;
    const title = slice.querySelector(".recipe-title").innerText;
    const imgSrc = slice.querySelector(".recipe-img").src;

    console.log(`선택된 레시피: ${title} (ID: ${selectedRecipeId})`);

    // 모달에 정보 표시
    document.getElementById("modalTitle").innerText = title;
    document.getElementById("modalImg").src = imgSrc;
    document.getElementById("modalImg").alt = title;

    // 모달 열기
    document.getElementById("modal").style.display = "flex";
}

// ==========================================
// 모달 닫기
// ==========================================
function closeModal() {
    document.getElementById("modal").style.display = "none";
    console.log('모달 닫기');
}

// ==========================================
// 상세보기 페이지로 이동
// ==========================================
function openDetail() {
    if (selectedRecipeId) {
        console.log(`상세 페이지로 이동: ${selectedRecipeId}`);
        selectDetail(selectedRecipeId);
    } else {
        console.error('선택된 레시피 ID가 없습니다.');
    }
}

// ==========================================
// 레시피 상세 페이지로 이동 (Form Submit)
// ==========================================
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

// ==========================================
// 상단 탭 클릭 핸들러
// ==========================================
function handleSegmentClick(button, url) {
    console.log(`탭 이동: ${url}`);
    setTimeout(() => location.href = url, 300);
}

// ==========================================
// 로그아웃 확인
// ==========================================
function confirmLogout() {
    if (confirm("정말 로그아웃 하시겠습니까?")) {
        console.log('로그아웃 진행');
        location.href = "/logout";
    }
}