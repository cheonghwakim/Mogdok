<template lang="">
   <transition name="fade">
      <div v-show="isOpenFAQ" class="about-modal">
         <div class="accordion-wrapper">
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(0)">
                  <p class="title">💁🏻 몽실이네 독서실은 뭐에요?</p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[0]" class="ac-content">
                     <span class="strong">몽실이네 독서실</span>은 화상 공유 기반 자기 주도 학습 플랫폼입니다. <br />스터디 캘린더를 통해 공부 시간을 직관적으로 확인하고, 내 책상에 메모를 추가하거나
                     다른 사람의 책상을 보며 학습 의욕을 키울 수 있어요! <br />
                     <br />
                     더 다양한 기능을 추가하려 해요. <a href="https://www.notion.so/jusami/f54d54e271d84d46abe54ea0a56151ec#b5ab970d14b34986bd50ca66dcbd13e3" target="blank">여기</a>에서 새소식을
                     전해드릴게요!
                  </div>
               </transition>
            </div>
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(1)">
                  <p class="title">🤔 최소 사양이 궁금해요</p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[1]" class="ac-content">
                     <div v-for="(spec, index) in specInfo" :key="'spec-' + index">
                        <p class="strong">{{ spec.title }}</p>
                        <li v-for="(content, index) in spec.contents" :key="'spec-li-' + index" style="padding-left:15px">
                           {{ content }}
                        </li>
                     </div>
                  </div>
               </transition>
            </div>
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(2)">
                  <p class="title">
                     🌏 최소 네트워크 속도가 궁금해요
                  </p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[2]" class="ac-content">
                     몽독을 이용하시면서 자주 끊기는 현상이 발생한다면 아래 사이트에서 인터넷 속도 측정을 해보시는 것을 권장드립니다!<br />
                     속도는 <a href="https://www.speedcheck.org/ko/" target="blank">여기</a>에서 체크해주세요.<br />
                     다운로드 속도, 업로드 속도 모두 10Mbps 이상이 나와야 안정적으로 사용하실 수 있습니다!
                  </div>
               </transition>
            </div>
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(3)">
                  <p class="title">❗️ 공부시작 버튼이 눌리지 않아요</p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[3]" class="ac-content">
                     카메라 권한 설정 허용을 반드시 해주셔야 합니다!<br />
                     <img src="https://mongdok.s3.us-east-2.amazonaws.com/camera.png" style="width:100%" /><br />
                     카메라 권한 허용을 해도 카메라가 안켜진다면?<br />
                     ⇒ 시작표시줄 > 설정 > 개인정보 > 카메라 > '앱에서 카메라에 액세스 하도록 허용', '데스크톱 앱에서 카메라를 사용할 수 있도록 허용' 에서 '켬'으로 해줘야 정상적으로 카메라가 나오게
                     됩니다.
                  </div>
               </transition>
            </div>
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(4)">
                  <p class="title">📷 카메라를 끄고 공부할 수 있나요?</p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[4]" class="ac-content">
                     몽실이네 독서실은 사용자와 공부하는 모습을 공유하며 학습의욕을 자극받는 플랫폼입니다.<br />
                     따라서 몽실이네 독서실을 이용하기 위해선 꼭 카메라를 켜주세요😘
                  </div>
               </transition>
            </div>
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(5)">
                  <p class="title">🎤 실수로 마이크가 켜지면 어쩌죠?</p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[5]" class="ac-content">
                     몽실이네 독서실은 소리가 들리지 않아요.<br />
                     영상을 공유하고 소리는 걱정하지 말고 공부에 집중해주세요~! 😉
                  </div>
               </transition>
            </div>
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(6)">
                  <p class="title">🖥 화면이 부드럽지 않게 나와요</p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[6]" class="ac-content">
                     몽실이네 독서실은 여러 사람이 화상 공유하는 서비스에요.<br />
                     컴퓨터에 부담이 가지 않도록 쾌적한 환경을 위해 영상 프레임을 의도적으로 낮췄어요. 🤫<br />
                  </div>
               </transition>
            </div>
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(7)">
                  <p class="title">🚨 불량사용자를 신고하고 싶어요</p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[7]" class="ac-content">
                     스터디룸 내부의 분위기를 흐리는 불량 이용자 발견시 신고를 부탁드립니다!🚨<br />
                     <a href="https://docs.google.com/forms/d/e/1FAIpQLSd3Kw2ruc8p34dGy74RSD6bArRB5nQTh9_CUKU5fwJ8N0DdQw/viewform?usp=sf_link" target="blank">여기</a>로 접속하시어 불량 사용자 닉네임을
                     작성해주세요!<br />
                     관리자가 확인 후 조치해 드립니다!
                  </div>
               </transition>
            </div>
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(8)">
                  <p class="title">🐶 더 자세한 정보가 궁금해요!</p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[8]" class="ac-content">
                     더 자세한 내용은
                     <a href="https://www.notion.so/jusami/f54d54e271d84d46abe54ea0a56151ec#b5ab970d14b34986bd50ca66dcbd13e3" target="blank">여기</a>를 확인해주세요.
                  </div>
               </transition>
            </div>
         </div>
         <btn-rounded class="btnClose" :label="'CLOSE'" :color="'red'" :type="'floating'" @onClick="closeFAQ"></btn-rounded>
      </div>
   </transition>
</template>
<script>
import BtnRounded from '@/components/ui/BtnRounded';

export default {
   components: { BtnRounded },
   props: {
      isOpenFAQ: {
         type: Boolean,
         default: false,
      },
   },
   data() {
      return {
         isOpenList: [false, false, false, false, false, false, false, false],
         specInfo: [
            {
               title: '브라우저',
               contents: ['크롬 브라우저 최신버전 (IE는 지원하지 않아요)'],
            },
            {
               title: 'PC',
               contents: ['운영체제 : 윈도우 최소 7버전 이상', 'RAM : 4GB 이상', 'HDD : 1GB 이상 여유공간', 'CPU : 2Core 이상'],
            },
            {
               title: 'IOS',
               contents: ['운영체제 : Kitkat 이상', '저장공간 : 1GB 여유공간', 'RAM : 2GB 이상', '권장 브라우저 : Safari'],
            },
            {
               title: '안드로이드',
               contents: ['운영체제 : iOS 9.2 이상', '저장공간 : 1GB 여유공간', 'RAM : 2GB 이상'],
            },
         ],
      };
   },
   methods: {
      // 로딩되면 섹션별로 설정 부여
      // #JS : 빠른 구현을 위한 수동 구현으로 대체
      toggleFAQ: function(param) {
         this.$set(this.isOpenList, param, !this.isOpenList[param]);
      },

      closeFAQ: function() {
         this.$emit('onClick');
      },
   },
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

.about-modal {
   min-width: 300px;
   max-width: 600px;
   width: 80%;
   height: 80%;
   max-height: 900px;

   position: fixed;
   z-index: 111;
   top: 50%;
   left: 50%;
   transform: translate(-50%, -50%);

   background-color: rgb(255, 255, 255);
   box-shadow: 0px 9px 20px 0px #56565629;
   border-radius: 25px;

   /* padding: 20px; */

   .accordion-wrapper {
      height: 100%;
      padding-bottom: 90px;

      overflow-x: hidden;
      overflow-y: auto;

      .section {
         /* margin-bottom: 40px; */

         .ac-header {
            width: 100%;
            height: 50px;
            line-height: 50px;
            font-weight: 600;
            font-size: 14pt;
            color: rgb(58, 58, 58);
            letter-spacing: 2px;

            margin-top: 10px;
            padding-left: 20px;
            cursor: pointer;
            z-index: 20;

            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;

            @media all and (max-width: 500px) {
               & {
                  font-size: 80%;
               }
            }
         }

         .ac-content {
            color: rgb(106, 106, 106);
            line-height: 20pt;
            font-size: 11pt;

            border-top: 1px solid rgb(223, 223, 223);
            border-bottom: 1px solid rgb(223, 223, 223);
            background-color: rgba(235, 235, 235, 0.386);
            padding: 20px;
            padding-bottom: 20px;

            .strong {
               font-weight: 600;
            }

            a {
               font-weight: 600;
               color: black;
            }

            img {
               border-radius: 20px;
            }
         }
      }
   }

   .btnClose {
      position: absolute;
      width: 250px;
      bottom: 20px;
      left: 50%;
      transform: translateX(-50%);
      z-index: 111;

      &:hover {
         transform: translateX(-50%) translateY(-7px);
      }
   }
}

.slide-in-top-enter-active {
   -webkit-animation: slide-in-right 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
   animation: slide-in-right 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}
.slide-in-top-leave-active {
   -webkit-animation: slide-in-right 0.3s reverse cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
   animation: slide-in-right 0.3s reverse cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}

@-webkit-keyframes slide-in-right {
   0% {
      -webkit-transform: translateY(-50px);
      transform: translateY(-50px);
      opacity: 0;
   }
   100% {
      -webkit-transform: translateY(0);
      transform: translateY(0);
      opacity: 1;
   }
}
@keyframes slide-in-right {
   0% {
      -webkit-transform: translateY(-50px);
      transform: translateY(-50px);
      opacity: 0;
   }
   100% {
      -webkit-transform: translateY(0);
      transform: translateY(0);
      opacity: 1;
   }
}
</style>
