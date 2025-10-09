<template>
    <div class="container">
        <div class="title-section">
            <h2>{{ currentDate }}</h2>
            <h2>{{ userName }}홍길동님의 오늘의 기록</h2>
            <!-- 이 부분에서 userName을 사용자의 아이디로 가져올 생각이라서 일단 반응형으로 넣어놓고 나중에 홍길동은 지우면 됩니다-->
            <!-- 저기서 보호자로 로그인했을 때 환자의 아이디를 가져오는 방법도 있는데 일단은 userName으로 남겨둘게요-->
        </div>

        <div class="survey-wrapper">
            <!-- 식사 섹션 -->
            <div class="survey-section">
                <h3 class="section-title">식사</h3>
                <div v-for="(question, index) in mealSurvey" :key="index" class="question-item">
                    <div class="question-text">
                        <MealMarker />
                        {{ question.question }}
                    </div>
                    <div class="answer-buttons">
                        <button v-for="(ans, ansIdx) in question.ans" :key="ansIdx"
                            :class="['answer-btn', { selected: isSelected('meal', index, ansIdx) }]"
                            @click="answerQuestion('meal', index, ansIdx)">
                            {{ ans }}
                        </button>
                    </div>
                </div>
            </div>

            <!-- 약 복용 섹션 -->
            <div class="survey-section">
                <h3 class="section-title">약 복용</h3>
                <div v-for="(question, index) in mediSurvey" :key="index" class="question-item">
                    <div class="question-text">
                        <MediMarker />
                        {{ question.question }}
                    </div>
                    <div class="answer-buttons">
                        <button v-for="(ans, ansIdx) in question.ans" :key="ansIdx"
                            :class="['answer-btn', { selected: isSelected('medi', index, ansIdx) }]"
                            @click="answerQuestion('medi', index, ansIdx)">
                            {{ ans }}
                        </button>
                    </div>
                </div>
            </div>

            <!-- 행동 섹션 -->
            <div class="survey-section">
                <h3 class="section-title">행동</h3>
                <div v-for="(question, index) in acitivySurvey" :key="index" class="question-item">
                    <div class="question-text">
                        <ActivityMarker />
                        {{ question.question }}
                    </div>
                    <div class="answer-buttons">
                        <button v-for="(ans, ansIdx) in question.ans" :key="ansIdx"
                            :class="['answer-btn', { selected: isSelected('activity', index, ansIdx) }]"
                            @click="answerQuestion('activity', index, ansIdx)">
                            {{ ans }}
                        </button>
                    </div>
                </div>
            </div>

            <!-- 감정 섹션 -->
            <div class="survey-section">
                <h3 class="section-title">감정</h3>
                <div v-for="(question, index) in feelSurvey" :key="index" class="question-item">
                    <div class="question-text">
                        <HappyMarker />
                        {{ question.question }}
                    </div>
                    <div class="answer-buttons">
                        <button v-for="(ans, ansIdx) in question.ans" :key="ansIdx"
                            :class="['answer-btn', { selected: isSelected('feel', index, ansIdx) }]"
                            @click="answerQuestion('feel', index, ansIdx)">
                            {{ ans }}
                        </button>
                    </div>
                </div>
            </div>

            <!-- 특이사항 섹션 -->
            <div class="survey-section">
                <h3 class="section-title">특이사항</h3>
                <div v-for="(question, index) in specialSurvey" :key="index" class="question-item">
                    <div class="question-text">
                        <AlertMarker />
                        {{ question.question }}
                    </div>
                    <div class="answer-buttons">
                        <button v-for="(ans, ansIdx) in question.ans" :key="ansIdx"
                            :class="['answer-btn', { selected: isSelected('special', index, ansIdx) }]"
                            @click="answerQuestion('special', index, ansIdx)">
                            {{ ans }}
                        </button>
                    </div>
                </div>
            </div>
            <div class="btn-group">
                <button class="btn-back" @click="goBack">돌아가기</button>
                <button class="btn-submit" @click="handleSubmit">제출하기</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import MealMarker from '@/components/MealMarker.vue' //
import MediMarker from '@/components/MediMarker.vue' // 
import ActivityMarker from '@/components/ActivityMarker.vue' //
import HappyMarker from '@/components/HappyMarker.vue'
import AlertMarker from '@/components/AlertMarker.vue' //
import axios from 'axios'

const userName = ref('홍길동')

const currentDate = ref('')
let intervalId = null

function updateDateTime() {
    const now = new Date()
    const yyyy = now.getFullYear()
    const mm = String(now.getMonth() + 1).padStart(2, '0')
    const dd = String(now.getDate()).padStart(2, '0')
    currentDate.value = `${yyyy}년 ${mm}월 ${dd}일`


}
onMounted(() => {
    updateDateTime()
    intervalId = setInterval(updateDateTime, 1000)
})

// 컴포넌트 언마운트 시 타이머 정리
onUnmounted(() => {
    clearInterval(intervalId)
})



const selectedAnswers = ref({
    meal: { 0: 0, 1: 0, 2: 0 },
    medi: { 0: 0, 1: 0 },
    activity: { 0: 0, 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 },
    feel: { 0: 0, 1: 0, 2: 0, 3: 0, 4: 0 },
    special: { 0: 0, 1: 0, 2: 0, 3: 0, 4: 0 }
})

// API 기본 URL 설정
const API_BASE_URL = 'http://localhost:8080/api'

// 사용자 ID (실제로는 로그인 정보에서 가져와야 함)
const userId = ref('1')  // 임시로 고정값 사용

// 제출하기 함수 구현
async function handleSubmit() {
    try {
        // 1. 모든 질문에 답변했는지 확인
        if (!validateAnswers()) {
            alert('모든 질문에 답변해 주세요.')
            return
        }

        // 2. 서버로 전송할 데이터 구성
        const requestData = {
            userId: userId.value,               // 숫자 ID 사용
            recordDate: getCurrentDate(),       // YYYY-MM-DD 형식
            mealAnswers: selectedAnswers.value.meal,
            medicationAnswers: selectedAnswers.value.medi,  // 프론트는 medi, 백엔드는 medication
            activityAnswers: selectedAnswers.value.activity,
            emotionAnswers: selectedAnswers.value.feel,     // 프론트는 feel, 백엔드는 emotion
            specialAnswers: selectedAnswers.value.special
        }

        console.log('전송할 데이터:', requestData)

        // 3. 로딩 상태 표시 (선택사항)
        const submitButton = document.querySelector('.btn-submit')
        const originalText = submitButton.textContent
        submitButton.textContent = '저장 중...'
        submitButton.disabled = true

        // 4. API 호출
        const response = await axios.post(`${API_BASE_URL}/record`, requestData, {
            headers: {
                'Content-Type': 'application/json'
            }
        })

        // 5. 응답 처리
        if (response.data.success) {
            alert('오늘의 기록이 성공적으로 저장되었습니다!')
            console.log('저장된 데이터:', response.data.data)
            
            // 성공 후 처리 (메인 페이지로 이동 등)
            // router.push('/main') // 라우터 사용 시
            // 또는 페이지 새로고침
            // window.location.reload()
        } else {
            alert('저장에 실패했습니다: ' + response.data.message)
        }

    } catch (error) {
        console.error('API 호출 오류:', error)
        
        if (error.response) {
            // 서버에서 응답을 받았지만 오류 상태
            alert(`서버 오류: ${error.response.data.message || '알 수 없는 오류가 발생했습니다.'}`)
        } else if (error.request) {
            // 요청이 만들어졌지만 응답을 받지 못함
            alert('서버에 연결할 수 없습니다. 네트워크 연결을 확인해 주세요.')
        } else {
            // 요청을 만드는 중에 오류 발생
            alert('요청 처리 중 오류가 발생했습니다.')
        }
    } finally {
        // 로딩 상태 해제
        const submitButton = document.querySelector('.btn-submit')
        if (submitButton) {
            submitButton.textContent = '제출하기'
            submitButton.disabled = false
        }
    }
}

// 답변 유효성 검사 함수
function validateAnswers() {
    // 식사 섹션 체크 (3개 질문)
    for (let i = 0; i < 3; i++) {
        if (selectedAnswers.value.meal[i] === undefined) {
            return false
        }
    }
    
    // 복약 섹션 체크 (2개 질문)
    for (let i = 0; i < 2; i++) {
        if (selectedAnswers.value.medi[i] === undefined) {
            return false
        }
    }
    
    // 활동 섹션 체크 (6개 질문)
    for (let i = 0; i < 6; i++) {
        if (selectedAnswers.value.activity[i] === undefined) {
            return false
        }
    }
    
    // 감정 섹션 체크 (5개 질문)
    for (let i = 0; i < 5; i++) {
        if (selectedAnswers.value.feel[i] === undefined) {
            return false
        }
    }
    
    // 특이사항 섹션 체크 (5개 질문)
    for (let i = 0; i < 5; i++) {
        if (selectedAnswers.value.special[i] === undefined) {
            return false
        }
    }
    
    return true
}

// 현재 날짜를 YYYY-MM-DD 형식으로 반환
function getCurrentDate() {
    const now = new Date()
    const year = now.getFullYear()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const day = String(now.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
}

// 돌아가기 함수
function goBack() {
    // router.go(-1) // Vue Router 사용 시
    // 또는 특정 페이지로 이동
    // router.push('/main')
    // 또는 브라우저 뒤로가기
    window.history.back()
}


onMounted(() => {
    updateDateTime()
    intervalId = setInterval(updateDateTime, 1000)
})

onUnmounted(() => {
    clearInterval(intervalId)
})

function answerQuestion(category, qIdx, ansIdx) {
    selectedAnswers.value[category][qIdx] = ansIdx
}

function isSelected(category, qIdx, ansIdx) {
    return selectedAnswers.value[category][qIdx] === ansIdx
}

const mealSurvey = [
    { question: '식사를 거르신 적이 있나요?', ans: ['없음', '아침', '점심', '저녁'] },
    { question: '간식을 드셨나요?', ans: ['예', '아니요'] },
    { question: '물을 충분히 마셨나요?', ans: ['많이 먹음', '보통 먹음', '적게 먹음'] }
]

const mediSurvey = [
    { question: '약 복용을 잊으신 적이 있나요?', ans: ['없음', '아침', '점심', '저녁'] },
    { question: '추가로 복용한 약(수면제, 진통제 등)이 있나요?', ans: ['예', '아니요'] }
]

const acitivySurvey = [
    { question: '잊은 약속 횟수는 몇 번인가요?', ans: ['0회', '1~2회', '3회 이상'] },
    { question: '물건을 잃어버린 횟수는 몇 번인가요?', ans: ['0회', '1~2회', '3회 이상'] },
    { question: '외출 중 길을 잃어버린 적이 있나요?', ans: ['예', '아니요'] },
    { question: '오늘 날짜를 올바르게 알고 있나요?', ans: ['예', '아니요'] },
    { question: '화장실을 혼자 이용하셨나요?', ans: ['예', '도움 필요'] },
    { question: '넘어지거나 비틀거린 적이 있나요?', ans: ['예', '아니요'] }
]

const feelSurvey = [
    { question: '전반적인 기분 상태는 어떠셨나요?', ans: ['매우 좋음', '좋음', '보통', '나쁨', '매우 나쁨'] },
    { question: '우울감을 느끼셨나요?', ans: ['없음', '경미', '심함'] },
    { question: '불안감을 느끼셨나요?', ans: ['없음', '경미', '심함'] },
    { question: '초조하거나 짜증을 느끼셨나요?', ans: ['없음', '경미', '심함'] },
    { question: '대화에 얼마나 적극적으로 참여하셨나요?', ans: ['적극', '보통', '소극'] }
]

const specialSurvey = [
    { question: '밤 중에 배회하거나 돌아다닌 적이 있나요?', ans: ['예', '아니요'] },
    { question: '같은 행동을 반복한 적이 있나요?', ans: ['예', '아니요'] },
    { question: '통증을 호소한 부분이 있나요?', ans: ['예', '아니요'] },
    { question: '체온이 평소와 달랐나요?', ans: ['예', '아니요'] },
    { question: '응급 상황이 발생한 적이 있나요?', ans: ['예', '아니요'] }
]
</script>

<style scoped>
.container {
    width: 450px;
    margin: 0 auto;
    padding: 20px;
    background-color: #f8f9fa;
}

.title-section {
    margin-bottom: 30px;
}

.title-section h2 {
    font-size: 24px;
    font-weight: 600;
    color: #333;
    margin: 5px 0;
}

.survey-wrapper {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 0;
}

.survey-section {
    background: white;
    border-radius: 12px;
    padding: 15px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.section-title {
    font-size: 20px;
    font-weight: 600;
    color: #333;
    margin-bottom: 20px;
    border-bottom: 2px solid #e9ecef;
    padding-bottom: 10px;
}

.question-item {
    margin-bottom: 24px;
}

.question-item:last-child {
    margin-bottom: 0;
}

.question-text {
    display: flex;
    align-items: center;
    font-size: 18px;
    font-weight: bold;
    color: #333;
    margin-bottom: 12px;
    gap: 8px;
}

.answer-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}

.answer-btn {
    padding: 8px 16px;
    border: 2px solid #e9ecef;
    border-radius: 20px;
    background: white;
    color: #444444;
    font-size: 16px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s ease;
}

.answer-btn:hover {
    border-color: #007bff;
    color: #007bff;
}

.answer-btn.selected {
    background: #7E88FF;
    border-color: #7E88FF;
    color: #FFFF;
}

.btn-group {
    display: flex;
    justify-content: flex-end;
    /* 오른쪽 정렬 */
    gap: 12px;
    margin-top: 0;
    padding-top: 12px;
    border-top: 1px solid #e9ecef;
}

/* 돌아가기 버튼 */
.btn-back {
    padding: 8px 16px;
    background: white;
    color: #666;
    border: 2px solid #e9ecef;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.2s;
}

.btn-back:hover {
    color: #333;
    background-color: #999;
}

/* 제출하기 버튼 */
.btn-submit {
    padding: 8px 16px;
    background: #007bff;
    color: white;
    border: 2px solid #007bff;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.2s;
}

.btn-submit:hover {
    background: #0056b3;
    border-color: #0056b3;
}


</style>