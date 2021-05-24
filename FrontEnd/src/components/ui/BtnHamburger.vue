<template lang="">
   <div class="hamburger">
      <div v-wave class="button-wrapper" @click="toggleDropdown">
         <!-- 햄버거 버튼 -->
         <transition name="bounce">
            <svg v-show="!isOpenDropDown" width="50%" height="50%" viewBox="0 0 32 8" fill="none" xmlns="http://www.w3.org/2000/svg">
               <path
                  class="svg__dot"
                  d="M3.12512 0.0119091C6.21406 0.306761 8.1488 1.79889 7.99103 3.77351C7.80004 6.18593 5.93173 8.15161 3.96378 7.99078C1.64707 7.80315 -0.254452 6.5612 0.0278709 4.57765C0.501176 1.31641 1.35645 -0.148919 3.12512 0.0119091Z"
                  fill="black"
               />
               <path
                  class="svg__dot"
                  d="M15.1251 0.0119091C18.2141 0.306761 20.1488 1.79889 19.991 3.77351C19.8 6.18593 17.9317 8.15161 15.9638 7.99078C13.6471 7.80315 11.7455 6.5612 12.0279 4.57765C12.5012 1.31641 13.3564 -0.148919 15.1251 0.0119091Z"
                  fill="black"
               />
               <path
                  class="svg__dot"
                  d="M27.1251 0.0119091C30.2141 0.306761 32.1488 1.79889 31.991 3.77351C31.8 6.18593 29.9317 8.15161 27.9638 7.99078C25.6471 7.80315 23.7455 6.5612 24.0279 4.57765C24.5012 1.31641 25.3564 -0.148919 27.1251 0.0119091Z"
                  fill="black"
               />
            </svg>
         </transition>
         <!-- 닫기버튼 -->
         <transition name="bounce">
            <svg v-show="isOpenDropDown" width="50%" height="50%" viewBox="0 0 25 35" fill="none" xmlns="http://www.w3.org/2000/svg">
               <path
                  d="M4.43729 0.126799C6.08116 3.90834 8.53928 6.94573 11.3047 10.0894C12.2009 8.88459 12.9383 7.86201 13.7065 6.85967C15.294 4.78412 17.1632 3.01231 19.5752 1.90367C20.2154 1.61006 20.963 1.47337 21.6697 1.42781C22.6837 1.362 23.6721 1.77205 23.7028 2.8402C23.7182 3.49323 23.1907 4.39432 22.6274 4.79425C18.9658 7.37096 17.4397 11.3854 15.463 15.0707C15.2069 15.5517 15.719 16.6704 16.1851 17.2526C18.5254 20.1786 20.9169 23.0692 23.3904 25.8838C25.1162 27.848 25.4286 28.739 24.4505 30.1918C23.4416 31.6852 21.4547 32.2522 19.5035 31.4675C16.7996 30.3792 15.2377 28.0404 13.5323 25.8838C12.969 25.1751 12.4928 24.4006 11.7758 23.3679C10.3931 25.656 9.15381 27.7214 7.90427 29.7767C7.18731 30.9563 6.49085 32.151 5.69708 33.2748C4.62677 34.7884 2.93169 35.3605 1.60533 34.7732C0.284091 34.1911 -0.350924 32.561 0.197032 30.7335C0.560629 29.5135 1.05225 28.3087 1.66166 27.1899C3.23384 24.3044 4.84186 21.4391 6.54718 18.6295C7.55603 16.9691 7.34607 15.5365 6.21943 14.0785C3.96615 11.1576 2.35301 7.89238 0.954953 4.51582C0.883258 4.3437 0.775715 4.19183 0.714262 4.01971C-0.320198 1.04814 1.05737 -0.465491 4.43729 0.126799Z"
                  fill="black"
               />
            </svg>
         </transition>
      </div>

      <transition name="slide-down">
         <div v-show="isOpenDropDown" class="dropdown-menu">
            <p class="menu" @click="goBugURL">🐞 버그 신고</p>
            <p class="menu" @click="goReportURL">🚨 불량 유저</p>
            <p class="menu" @click="toggleModalGuide">🚴‍♂️ 퀵 가이드</p>
            <p class="menu" @click="toggleModalFAQ">🗣 질의응답</p>
            <p class="menu" @click="toggleModalAbout">🐶 만든 사람들</p>
         </div>
      </transition>

      <modal-faq :isOpenFAQ="isOpenFAQ" @onClick="toggleModalFAQ"></modal-faq>
      <modal-guide :isOpenGuide="isOpenGuide" @onClick="toggleModalGuide"></modal-guide>
      <modal-about :isOpenAbout="isOpenAbout" @onClick="toggleModalAbout"></modal-about>
   </div>
</template>
<script>
import ModalFaq from '@/components/ui/ModalFaq';
import ModalGuide from '@/components/ui/ModalGuide';
import ModalAbout from '@/components/ui/ModalAbout';

export default {
   components: { ModalFaq, ModalGuide, ModalAbout },
   props: {},
   data() {
      return {
         isOpenFAQ: false,
         isOpenAbout: false,
         isOpenGuide: false, // 가이드 토글용

         isOpenDropDown: false,
      };
   },
   methods: {
      toggleDropdown() {
         this.isOpenDropDown = !this.isOpenDropDown;
      },

      goBugURL() {
         window.open('https://forms.gle/Ggp8dWPia4jfHthU7', '_blank');
      },

      goReportURL() {
         window.open('https://docs.google.com/forms/d/e/1FAIpQLSd3Kw2ruc8p34dGy74RSD6bArRB5nQTh9_CUKU5fwJ8N0DdQw/viewform?usp=sf_link', '_blank');
      },

      toggleModalFAQ() {
         this.isOpenFAQ = !this.isOpenFAQ;
      },

      toggleModalGuide() {
         this.isOpenGuide = !this.isOpenGuide;
      },

      toggleModalAbout: function() {
         this.isOpenAbout = !this.isOpenAbout;
      },

      onClick: function() {
         this.$emit('onClick');
      },
   },
};
</script>
<style scoped lang="scss">
.hamburger {
   position: relative;
   z-index: 40;

   .button-wrapper {
      cursor: pointer;
      width: 40px;
      height: 40px;
      border-radius: 50%;

      svg {
         margin-top: 10px;
         margin-left: 10px;
      }

      &:hover .svg__dot {
         fill: #3e3e3e;
      }
   }

   .dropdown-menu {
      position: fixed;
      top: 50px;
      z-index: 45;

      padding: 20px;
      border: 2px solid rgb(46, 46, 46);
      border-radius: 20px;
      box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);

      background-color: white;

      .menu {
         cursor: pointer;
         font-weight: 600;
         color: rgb(46, 46, 46);
         font-size: 10pt;
         margin-bottom: 10px;

         &:last-child {
            margin-bottom: 0px;
         }
      }
   }
}

.slide-down-enter-active,
.slide-down-leave-active {
   transition: all 0.5s ease;
}
.slide-down-enter,
.slide-down-leave-to {
   transform: translateX(-300px);
   opacity: 0;
}

.bounce-enter-active {
   animation: bounce-in 0.5s;
}
/* .bounce-leave-active {
   animation: bounce-in 0.5s reverse;
} */
@keyframes bounce-in {
   0% {
      transform: scale(0);
   }
   50% {
      transform: scale(1.5);
   }
   100% {
      transform: scale(1);
   }
}
</style>
