<template>
   <div class="join">
      <div class="container">
         <p class="kyoboHand" style="color:gray; font-size:10pt">독서실 가입서를 작성해주세요!</p>
         <!-- <v-lottie-player name="scooterAnim" :width="'200px'" :height="'200px'" loop path="https://assets7.lottiefiles.com/private_files/lf30_z8mfdyic.json" /> -->
         <div class="content">
            <!-- STPE 1 : 닉네임 -->
            <transition name="slide-left">
               <div v-show="first" class="step-box first">
                  <input v-model="userName" placeholder="닉네임을 입력하세요" type="text" maxlength="10" @keyup.enter="checkuserNameDuplicate" />

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
                  <div class="category" :class="{ 'cate--selected': selectCategory === item }" v-for="item in categories" :key="item" @click="clickCategory(item)">
                     {{ item }}
                  </div>
               </div>
            </transition>
            <!-- STPE 3 : 다짐 입력 -->
            <transition name="slide-left">
               <div v-show="third" class="step-box third">
                  <textarea v-model="promise" placeholder="공부 목표를 적고, 학습의 능률을 높이세요💪" />
                  <span class="info">다짐을 적으면, 독서실에 입장하실 수 있어요!</span>
               </div>
            </transition>
         </div>
         <!-- 최종 버튼 -->
         <transition name="slide-left">
            <button v-show="last" class="submit-button" @click="submit()">
               독서실 입장하기
            </button>
         </transition>
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
         // STEP1 : 닉네임 중복
         first: false,
         isShowInfo: false, // 닉네임 필드 하단 안내 표시 여부
         isCheckNameDup: false, // 닉네임 중복 여부 체크 - 최종적으로 체크하는 역할
         isValid: false, //유효성 검사 flag - 최종적으로 체크하는 역할
         isError: false, // 에러일 경우 붉은 info박스
         userName: '',
         msg: '닉네임을 입력해주세요',

         // STEP2 : 카테고리 선택
         second: false,
         categories: ['초등학생', '중학생', '고등학생', '고3/N수생', '대학생', '대학원', '편입준비생', '공무원', '고시생', '취업준비생', '자격증', '기타'],
         selectCategory: '',

         // STEP3 : 다짐 입력
         third: false,
         promise: '',

         // STEP4 : 제출 단계
         last: false,
      };
   },

   watch: {
      // userName 작성할 때마다 검사
      userName: function() {
         this.isValid = this.validation();
         this.isCheckNameDup = false; // 새로 작성시, 중복 체크 여부 초기화
      },

      // 다짐이 작성되어 있으면, 제출 버튼 활성화
      promise: function() {
         if (this.promise.length === 0) {
            this.last = false;
         } else {
            this.last = true;
         }
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
      // STEP1 : 닉네임 중복
      validation: function() {
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
      checkuserNameDuplicate: function() {
         // 비워진 상태에서 실행시 거르기
         if (this.userName.length == 0) {
            //입력 안한경우
            this.isShowInfo = true;
            this.msg = '닉네임을 입력해주세요';
            this.isError = true;
            return;
         }

         // 유효성 검사 불통시 거르기
         if (!this.isValid) {
            //유효성 검사 통과못함
            alert('닉네임을 다시 확인해주세요');
            return;
         }

         //닉네임 중복검사
         checkUserNameDuplicated(
            this.userName,
            (res) => {
               if (res.data) {
                  this.msg = '사용 가능한 닉네임입니다.';
                  this.isShowInfo = true;
                  this.isError = false;
                  this.second = true;
                  this.isCheckNameDup = true;
               } else {
                  this.msg = '이미 사용중인 닉네임입니다.';
                  this.isShowInfo = true;
                  this.isError = true;
                  this.second = false;
                  this.isCheckNameDup = false;
               }
            },
            () => {}
         );
      },

      // STEP 2 : 카테고리 선택
      clickCategory: function(item) {
         this.selectCategory = item;
         this.third = true;
      },

      // STEP 3 : 다짐 입력

      // LAST : 최종 제출
      submit: function() {
         if (!this.isValid) {
            //유효성 검사 통과못함
            alert('닉네임을 다시 확인해주세요');
         } else if (!this.isCheckNameDup) {
            //중복체크안함
            alert('닉네임 중복 체크를 다시 해주세요');
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

   /* border: 1px solid pink; */

   .container {
      display: inherit;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      width: 100vw;
      height: 90vh;

      /* border: 1px solid rgb(194, 23, 52); */

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
            justify-content: center;
         }

         /* STEP 1 : 닉네임 */
         .first {
            position: relative;
            height: 180px;

            input {
               font-family: 'KyoboHand';
               font-size: 12pt;

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

         // STEP 2 : 카테고리
         .second {
            .category {
               cursor: pointer;

               $cate-color: #c9c9c9;
               $cate-color-sel: #a7a7a7;

               border-radius: 20px;
               margin: 5px;
               text-align: center;
               padding: 8px 10px;
               font-size: 10pt;
               color: $cate-color;
               border: $cate-color 1px solid;

               &.cate--selected {
                  color: rgb(255, 255, 255);
                  background-color: $cate-color-sel;
                  border: none;
                  font-weight: 600;
               }
            }
         }

         // STEP 3 : 다짐 입력
         .third {
            textarea {
               font-family: 'KyoboHand';
               font-size: 12pt;
               line-height: 20px;

               padding: 30px 10px;

               width: 100%;
               /* height: 100px; */
               resize: none;

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
               color: rgb(114, 114, 114);
               font-weight: 500;
               font-size: 8pt;
               margin-top: 10px;
            }
         }
      } // content-end

      // LAST : 최종 제출
      .submit-button {
         width: 320px;
         min-height: 45px;

         font-size: 10pt;
         text-transform: uppercase;
         letter-spacing: 2.5px;
         font-weight: 500;
         color: rgb(63, 63, 63);
         background-color: #fff;
         border: none;
         border-radius: 45px;
         box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);
         transition: all 0.3s ease 0s;
         cursor: pointer;
         outline: none;

         display: block;
         margin: 0px auto;

         &:hover {
            background-color: #2ee59d;
            box-shadow: 0px 15px 20px rgba(46, 229, 157, 0.4);
            color: #fff;
            transform: translateY(-7px);
         }
      }
   } // container-end
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
