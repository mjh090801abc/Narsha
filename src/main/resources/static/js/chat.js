const chatBox = document.getElementById('chat-box');
const input = document.getElementById('user-input');
const sendBtn = document.getElementById('send-btn');
const recipesDiv = document.getElementById('recipes');

let isStreaming = false;

/* 메시지 추가 */
function addMessage(text, type) {
    const msg = document.createElement('div');
    msg.className = `message ${type}`;

    const bubble = document.createElement('div');
    bubble.className = 'bubble';
    bubble.textContent = text;

    msg.appendChild(bubble);
    chatBox.appendChild(msg);
    chatBox.scrollTop = chatBox.scrollHeight;

    return bubble;
}

/* ✅ 이전 대화 로드 (USER + AI 전부 표시) */
async function loadHistory() {
    try {
        const res = await fetch(`/api/chat/history?userId=${userId}`);
        const list = await res.json();

        list.forEach(m => {
            addMessage(
                m.content,
                m.role === 'user' ? 'user' : 'bot'
            );
        });
    } catch (e) {
        console.error(e);
    }
}

/* 전송 이벤트 */
sendBtn.addEventListener('click', () => {
    if (!isStreaming) sendMessage();
});

input.addEventListener('keyup', e => {
    if (e.key === 'Enter') {
        e.preventDefault(); // ⭐ form submit 방지
        if (!isStreaming) sendMessage();
    }
});

async function sendMessage() {
    const text = input.value.trim();
    if (!text) return;

    isStreaming = true;
    input.value = '';

    /* user 메시지 즉시 출력 */
    addMessage(text, 'user');

    /* DB 저장 */
    await fetch('/api/chat/send', {
        method: 'POST',
        headers: {'Content-Type':'application/json'},
        body: JSON.stringify({ userId, message: text })
    });

    /* bot 말풍선 (stream 전용) */
    const botBubble = addMessage('', 'bot');

    const es = new EventSource(`/api/chat/stream?userId=${userId}`);
    let acc = '';

    es.onmessage = e => {
        acc += e.data;
        botBubble.textContent = acc;
        chatBox.scrollTop = chatBox.scrollHeight;
    };

    es.addEventListener('end', () => {
        es.close();
        isStreaming = false;

        /* 레시피 JSON 파싱 */
        try {
            const start = acc.indexOf('[');
            if (start !== -1) {
                const parsed = JSON.parse(acc.substring(start));
                if (Array.isArray(parsed)) showRecipes(parsed);
            }
        } catch (e) {
            console.warn('레시피 파싱 실패', e);
        }
    });

    es.onerror = () => {
        es.close();
        isStreaming = false;
    };
}

/* 레시피 카드 */
function showRecipes(list) {
    recipesDiv.innerHTML = '';
    list.forEach(r => {
        const card = document.createElement('div');
        card.className = 'recipe-card';

        card.innerHTML = `
            <img src="${r.imageUrl || 'https://via.placeholder.com/300x200'}">
            <h4>${r.title || '제목 없음'}</h4>
            <p>${r.description || ''}</p>
        `;
        recipesDiv.appendChild(card);
    });
}

/* 초기 실행 */
loadHistory();
