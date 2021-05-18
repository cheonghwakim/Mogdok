<template lang="">
   <transition name="fade">
      <div v-show="isOpenFAQ" class="about-modal">
         <div class="accordion-wrapper">
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(0)">
                  <p class="title">🤔 몽실이네 독서실을 원활히 이용하려면?</p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[0]" class="ac-content">
                     <ul>
                        <li>Browser : 크롬 권장</li>
                        <li>OS : 크롬 권장</li>
                        <li>Browser : 크롬 권장</li>
                        <li>Browser : 크롬 권장</li>
                     </ul>
                  </div>
               </transition>
            </div>
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(1)">
                  <p class="title">🤔 몽실이네 독서실은 뭐하는 곳인가요?</p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[1]" class="ac-content">
                     모든 국민은 그 보호하는 자녀에게 적어도 초등교육과 법률이 정하는 교육을 받게 할 의무를 진다. 민주평화통일자문회의의 조직·직무범위 기타 필요한 사항은 법률로 정한다.
                     대통령·국무총리·국무위원·행정각부의 장·헌법재판소 재판관·법관·중앙선거관리위원회 위원·감사원장·감사위원 기타 법률이 정한 공무원이 그 직무집행에 있어서 헌법이나 법률을 위배한
                     때에는 국회는 탄핵의 소추를 의결할 수 있다. 모든 국민은 법률이 정하는 바에 의하여 국가기관에 문서로 청원할 권리를 가진다. 행정각부의 설치·조직과 직무범위는 법률로 정한다. 국정의
                     중요한 사항에 관한 대통령의 자문에 응하기 위하여 국가원로로 구성되는 국가원로자문회의를 둘 수 있다.
                  </div>
               </transition>
            </div>
            <div class="section">
               <div v-wave class="ac-header" @click="toggleFAQ(2)">
                  <p class="title">🤔 몽실이네 독서실 최소 사양이 궁금해요</p>
               </div>
               <transition name="slide-in-top">
                  <div v-show="isOpenList[2]" class="ac-content">
                     모든 국민은 그 보호하는 자녀에게 적어도 초등교육과 법률이 정하는 교육을 받게 할 의무를 진다. 민주평화통일자문회의의 조직·직무범위 기타 필요한 사항은 법률로 정한다.
                     대통령·국무총리·국무위원·행정각부의 장·헌법재판소 재판관·법관·중앙선거관리위원회 위원·감사원장·감사위원 기타 법률이 정한 공무원이 그 직무집행에 있어서 헌법이나 법률을 위배한
                     때에는 국회는 탄핵의 소추를 의결할 수 있다. 모든 국민은 법률이 정하는 바에 의하여 국가기관에 문서로 청원할 권리를 가진다. 행정각부의 설치·조직과 직무범위는 법률로 정한다. 국정의
                     중요한 사항에 관한 대통령의 자문에 응하기 위하여 국가원로로 구성되는 국가원로자문회의를 둘 수 있다.
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
         isOpenList: [false, false, false],
      };
   },
   methods: {
      // 로딩되면 섹션별로 설정 부여
      // #JS : 빠른 구현을 위한 수동 구현으로 대체
      // initFAQ: function() {
      //    var headers = document.querySelectorAll('.ac-header');
      //    var contents = document.querySelectorAll('.ac-content');

      //    this.openList = new Array(headers.length);

      //    for (let index = 0; index < headers.length; index++) {
      //       headers[index].setAttribute('data-index', index);
      //       contents[index].setAttribute('data-index', index);
      //    }
      // console.log(event.currentTarget.getAttribute('data-index'));
      // },
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
   width: 80vw;
   height: 80vh;
   max-height: 900px;

   position: fixed;
   z-index: 10;
   top: 50%;
   left: 50%;
   transform: translate(-50%, -50%);

   background-color: rgb(255, 255, 255);
   box-shadow: 0px 9px 20px 0px #56565629;
   border-radius: 25px;

   /* padding: 20px; */

   .accordion-wrapper {
      height: 100%;

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
         }
      }
   }

   .btnClose {
      position: absolute;
      width: 250px;
      bottom: 20px;
      left: 50%;
      transform: translateX(-50%);
      z-index: 20;

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
