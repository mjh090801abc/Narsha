let currentStepIndex = 0;
let steps = [];
let utterance = null;

function loadSteps() {
    steps = Array.from(document.querySelectorAll("[data-step]"))
        .map(step => step.querySelector("textarea").value.trim())
        .filter(text => text.length > 0);
}

function highlightStep(index) {
    document.querySelectorAll("[data-step]").forEach((el, i) => {
        el.style.backgroundColor = i === index ? "#fff3cd" : "";
    });
}

function startStepTTS() {
    window.speechSynthesis.cancel();
    loadSteps();
    currentStepIndex = 0;
    speakCurrentStep();
}

function speakCurrentStep() {
    if (currentStepIndex >= steps.length) {
        highlightStep(-1);
        return;
    }

    highlightStep(currentStepIndex);

    utterance = new SpeechSynthesisUtterance(steps[currentStepIndex]);
    utterance.lang = "ko-KR";

    utterance.onend = () => {
        currentStepIndex++;
        speakCurrentStep();
    };

    window.speechSynthesis.speak(utterance);
}

function stopStepTTS() {
    window.speechSynthesis.cancel();
    highlightStep(-1);
}
