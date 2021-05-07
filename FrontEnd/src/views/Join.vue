<template>
   <div class="join">
      <div class="container">
         <p>아래 정보를 입력하고, 공부를 시작하세요</p>
         <!-- <v-lottie-player name="scooterAnim" :width="'200px'" :height="'200px'" loop path="https://assets7.lottiefiles.com/private_files/lf30_z8mfdyic.json" /> -->
         <div class="content">
            <!-- STPE 1 : 닉네임 -->
            <transition name="slide-left">
               <div v-show="first" class="step-box first">
                  <input v-model="userName" placeholder="닉네임을 입력하세요" type="text" maxlength="10" />

                  <div v-show="isShowInfo" class="info" :class="{ 'valid--fail': isError }">
                     {{ msg }}
                  </div>
                  <div class="check-button" @click="checkuserNameDuplicate">
                     <div class="inner">
                        <img src="@/assets/img/emoji/sleuth-or-spy_1f575.png" alt="" />
                     </div>
                  </div>
               </div>
            </transition>
            <!-- STPE 2 : 카테고리 -->
            <transition name="slide-left">
               <div v-show="second" class="step-box second">
                  <button class="category" v-for="item in categories" :key="item">
                     {{ item }}
                  </button>
               </div>
            </transition>
            <!-- STPE 3 : 다짐 입력 -->
            <transition name="slide-left">
               <div v-show="last" class="step-box last">
                  <input v-model="promise" placeholder="다짐을 입력해주세요" type="text" />
               </div>
            </transition>
            <!-- 최종 버튼 -->
            <button color="red lighten-2" dark @click="submit()">
               가입하기
            </button>
         </div>
         <logo></logo>
      </div>
   </div>
</template>
<script>
import { checkUserNameDuplicated, signup } from '../api/user';
import Logo from '@/components/ui/Logo';

const expName = /^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]{2,10}$/;

export default {
   name: 'Join',
   components: { Logo },
   props: {},
   data() {
      return {
         first: true,
         second: true,
         last: true,

         isShowInfo: false, // 닉네임 필드 하단 안내 표시 여부
         isUse: false, // 닉네임 중복 여부 체크
         isError: false, // 에러일 경우 붉은 info박스

         userName: '',
         selectCategory: '',
         categories: ['초등학생', '중학생', '고등학생1,2학년', '고3n수생', '대학생', '대학원', '편입생', '공무원', '고시생', '취업준비생', '자격증', '기타'],
         msg: '닉네임을 입력해주세요',
         isValid: false, //유효성 검사 flag
         promise: '',
      };
   },

   watch: {
      userName() {
         this.isValid = this.validation();
      },
   },
   //lifecycle area
   created() {
      if (!this.$store.state.user.authToken && !localStorage.getItem('authToken')) this.$router.push({ name: 'Login' });
      setTimeout(() => {
         this.first = true;
      }, 500);
   },
   methods: {
      validation() {
         if (this.userName.length == 0) {
            this.isShowInfo = false;
            return false;
         } else if (!expName.test(this.userName.trim())) {
            //닉네임유효성검사
            this.msg = '2~10글자 사이의 한글,영문,숫자만 사용가능';
            this.isError = true;
            this.isShowInfo = true;
            return false;
         } else {
            //유효성 검사 통과
            this.msg = '';
            this.isShowInfo = false;
            return true;
         }
      },
      checkuserNameDuplicate() {
         // 비워진 상태에서 실행시 거르기
         if (this.userName.length == 0) {
            //입력 안한경우
            this.isShowInfo = true;
            this.msg = '닉네임을 입력해주세요';
            this.isError = true;
            return;
         }

         //닉네임 중복검사
         checkUserNameDuplicated(
            this.userName,
            (res) => {
               if (res.data) {
                  this.msg = '사용가능한 닉네임입니다.';
                  this.isShowInfo = true;
                  this.isError = false;
                  this.second = true;
               } else {
                  this.msg = '이미 사용중인 닉네임입니다.';
                  this.isShowInfo = true;
                  this.isError = true;
                  this.second = false;
               }
            },
            () => {}
         );
      },
      submit() {
         if (!this.isValid) {
            //유효성 검사 통과못함
            alert(this.msg);
         } else if (this.isUse == 0) {
            //중복체크안함
            alert('닉네임 중복체크를 해주세요');
         } else if (this.isUse == 1) {
            //닉네임 중복됨
            alert(this.msg);
         } else if (!this.selectCategory) {
            //카테고리 선택안함
            alert('카테고리를 선택해주세요');
         } else {
            const userInfo = {
               category: this.selectCategory,
               promise: this.promise,
               userName: this.userName,
            };
            signup(
               userInfo,
               () => {
                  alert('회원가입에 성공하였습니다.');
                  this.$router.push('/');
               },
               () => {
                  alert('회원가입에 실패하였습니다.');
               }
            );
         }
      },
   },
};
</script>
<style scoped lang="scss">
@import 'src/assets/css/common';

.join {
   width: 100%;
   height: 100%;

   display: flex;
   align-items: center;
   justify-content: center;

   border: 1px solid pink;

   .container {
      display: inherit;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      width: 100vw;
      height: 90vh;

      border: 1px solid rgb(194, 23, 52);

      .content {
         width: inherit;
         /* border: 1px solid rgb(15, 15, 198); */

         overflow-y: auto;
         overflow-x: hidden;

         .step-box {
            width: 320px;

            padding: 20px;

            background-color: rgb(255, 255, 255);
            box-shadow: 0px 9px 20px 0px #56565629;
            border-radius: 20px;

            margin: 20px auto;

            display: flex;
            flex-wrap: wrap;
         }

         .category {
            border-radius: 20px;
            margin: 5px;
            text-align: center;
            padding: 8px 10px;
            font-size: 10pt;
            color: rgb(45, 45, 45);
            border: 1px solid rgb(45, 45, 45);
         }

         /* 닉네임 입력 스텝 */
         .first {
            position: relative;
            height: 180px;

            input {
               font-family: 'KyoboHand';
               font-size: 14pt;

               width: 100%;
               height: 50px;
               text-align: center;
               border: none;

               background-color: rgb(244, 244, 244);
               border-radius: 30px;

               &:focus {
                  outline: none;
               }
               &::placeholder {
                  color: #2d2d2d;
               }
            }

            .info {
               margin-top: 15px;

               /* display: none; */

               position: absolute;
               left: 50%;
               top: 55px;
               transform: translateX(-50%);

               width: 70%;
               height: 20px;
               font-size: 8pt;
               text-align: center;
               line-height: 20px;
               z-index: 5;

               padding: 0px 10px;

               color: #ffffff;
               background-color: rgb(0, 135, 49);
               border-radius: 8px;

               &:after {
                  border-bottom: 0px solid transparent;
                  border-left: 8px solid transparent;
                  border-right: 8px solid transparent;
                  border-top: 8px solid rgb(0, 135, 49);
                  content: '';
                  position: absolute;
                  bottom: -8px;
                  left: 47%;
               }

               &.valid--fail {
                  color: rgb(255, 255, 255);
                  background-color: rgb(255, 68, 0);
                  &:after {
                     border-top: 8px solid rgb(255, 68, 0);
                  }
               }

               .valid--pass {
                  color: rgb(0, 135, 49);
               }
            }

            .check-button {
               cursor: pointer;
               margin-top: 6px;
               width: 50px;
               height: 50px;

               position: relative;
               left: 42%;
               bottom: 0px;

               display: flex;
               flex-direction: row;
               justify-content: center;
               align-items: center;
               z-index: 1;

               border-radius: 100%;
               background: #ffffff;
               box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.25);

               .inner {
                  width: 100%;
                  height: 100%;
                  border-radius: 100%;
                  background: #ffffff;
                  padding: 10px;
               }

               &::after {
                  @include rings(3s, 0s);
               }

               &::before {
                  @include rings(3s, 0.5s);
               }
               img {
                  width: 100%;
                  height: 100%;
               }
            }
         }
      }
   }
}

.slide-left-enter-active,
.slide-left-leave-active {
   transition: all 0.5s ease;
}
.slide-left-enter,
.slide-left-leave-to {
   opacity: 0;
   transform: translateX(500px);
}
</style>
