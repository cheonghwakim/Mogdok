<template lang="">
   <transition name="fade">
      <div v-show="isOpenAbout" class="about-modal">
         <div class="content-wrapper">
            <div class="title-wrapper">
               <p class="title">
                  안녕하세요.
               </p>
               <typical class="typicalWrapper title bold" :steps="['몽실이네 독서실의 개발자,', 1500, '몽실이 제자들 입니다 👋', 1500]" :loop="Infinity" :wrapper="'div'"></typical>
            </div>
            <div class="desc-wrapper">
               <span class="team">몽실이 제자들</span>은 <br /><span style="color:#1b0b88; font-weight:600">삼성 청년 SW 아카데미 4기</span> 개발자 5명으로 이루어진 팀입니다.<br />
               코로나 걱정 없는, 독서실 빌런 걱정 없는, <br /><span style="font-weight:600">세상에서 가장 귀여운 몽실이네 독서실</span>에서 공부하세요!
            </div>
            <div class="member-wrapper">
               <div v-for="(person, idx) in members" :key="idx" class="member">
                  <div class="person-wrapper">
                     <div class="pic" :style="{ 'background-image': 'url(' + person.src + ')' }"></div>
                     <div class="name">{{ person.name }}</div>
                     <div class="position">{{ person.position }}</div>
                  </div>
               </div>
            </div>
         </div>
         <div class="powered-wrapper">
            powered by.
            <img src="../../assets/img/about/openvidu.png" alt="" />
         </div>
         <btn-rounded class="btnClose" :label="'CLOSE'" :color="'red'" @onClick="closeAbout"></btn-rounded>
      </div>
   </transition>
</template>
<script>
import BtnRounded from '@/components/ui/BtnRounded';
import typical from 'vue-typical';

export default {
   components: { BtnRounded, typical },
   props: {
      isOpenAbout: {
         type: Boolean,
         default: false,
      },
   },

   data() {
      return {
         members: [
            {
               name: '박영철',
               src: 'https://mongdok.s3.us-east-2.amazonaws.com/%EB%B0%95%EC%98%81%EC%B2%A0.jpg',
               position: 'Front-End',
            },
            {
               name: '이주상',
               src: 'https://mongdok.s3.us-east-2.amazonaws.com/%EC%9D%B4%EC%A3%BC%EC%83%81.jpg',
               position: 'Front-End',
            },
            {
               name: '김한솔',
               src: 'https://mongdok.s3.us-east-2.amazonaws.com/%EA%B9%80%ED%95%9C%EC%86%94.jpg',
               position: 'Back-End',
            },
            {
               name: '김청화',
               src: 'https://mongdok.s3.us-east-2.amazonaws.com/%EA%B9%80%EC%B2%AD%ED%99%94.jpg',
               position: 'Back-End',
            },
            {
               name: '박유정',
               src: 'https://mongdok.s3.us-east-2.amazonaws.com/%EB%B0%95%EC%9C%A0%EC%A0%95.jpg',
               position: 'Back-End',
            },
         ],
      };
   },

   methods: {
      closeAbout: function() {
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
   z-index: 111;
   top: 50%;
   left: 50%;
   transform: translate(-50%, -50%);

   background-color: rgb(255, 255, 255);
   box-shadow: 0px 9px 20px 0px #56565629;
   border-radius: 25px;

   padding: 20px;

   overflow-x: hidden;
   overflow-y: auto;

   display: flex;
   flex-direction: column;
   justify-content: space-between;

   .title-wrapper {
      margin: 40px 0;
   }

   .title {
      font-size: 22pt;
      margin-bottom: 10px;
      font-weight: 200;
   }

   .team {
      font-weight: 600;
      color: #074f10;
   }

   .bold {
      font-weight: 400;
   }

   .desc-wrapper {
      border-top: 1px solid rgb(237, 237, 237);
      border-bottom: 1px solid rgb(237, 237, 237);
      padding: 30px 20px;
      color: gray;
      font-size: 10pt;
      font-weight: 400;
      letter-spacing: 1px;
      line-height: 22px;
   }

   .member-wrapper {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-around;
      width: 100%;

      margin: 40px 0px;

      /* 주상 */
      .member:nth-child(2) {
         .pic {
            background-size: 100px 100px;
         }
      }
      /* 유정 */
      .member:nth-child(5) {
         .pic {
            background-position: -55px -30px;
         }
      }

      .person-wrapper {
         display: flex;
         flex-direction: column;
         align-items: center;

         margin: 10px;

         font-weight: 600;
         font-size: 10pt;

         .pic {
            height: 80px;
            width: 80px;

            box-shadow: 0px 4px 15px rgb(0 0 0 / 20%);

            border-radius: 50%;
            background-repeat: no-repeat;
            background-position: center;
            background-size: 180px 180px;

            margin-bottom: 14px;
         }

         .position {
            margin-top: 6px;
            font-weight: 400;
            color: rgb(109, 109, 109);
         }
      }
   }

   .powered-wrapper {
      width: 100%;
      /* border: 1px dashed red; */

      display: flex;
      flex-direction: column;
      align-items: center;

      font-weight: 400;
      font-size: 8pt;
      letter-spacing: 2px;
      color: rgb(181, 181, 181);

      img {
         margin-top: 20px;
         width: 100px;
      }
   }

   .btnClose {
      width: 250px;

      &:hover {
         transform: translateY(-7px);
      }
   }
}
</style>
