<template lang="">
   <div class="div-faq">
      <div v-wave class="btn-faq-wrapper" @click="toggleModalFAQ">
         <svg width="100%" height="100%" viewBox="0 0 22 38" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path
               d="M10.56 0.610107C18.76 1.85011 23.02 7.19011 20.12 15.1701C19.1 17.9801 17.87 20.7401 16.45 23.3601C16.03 24.1301 14.18 24.9801 13.71 24.6801C12.74 24.0401 11.57 22.5701 11.71 21.6101C12 19.5101 12.93 17.4601 13.81 15.4801C15.72 11.1601 15.83 9.18011 13.01 7.17011C11.31 5.96011 7.83 5.90011 6.18 7.13011C4.99 8.01011 5.16 11.1401 6.24 12.5601C6.82 13.3201 6.95 14.4101 7.29 15.3501C5.86 15.2801 3.83 15.7801 3.11 15.0301C0.889999 12.7201 -0.530002 9.53011 0.419998 6.42011C1.35 3.38011 4.21 1.75011 7.38 1.09011C8.51 0.840108 9.68 0.730107 10.56 0.610107Z"
               fill="black"
            />
            <path
               d="M14.83 37.6799C12.58 37.6499 12 36.1899 11.99 34.2799C11.98 32.2699 13.74 30.1499 15.41 30.1099C17.53 30.0599 19.16 31.5299 19.14 33.4499C19.1 35.7999 17.17 37.6899 14.83 37.6799Z"
               fill="black"
            />
         </svg>
      </div>

      <modal-faq :isOpenFAQ="isOpenFAQ" @onClick="toggleModalFAQ"></modal-faq>
   </div>
</template>
<script>
import ModalFaq from '@/components/ui/ModalFaq';

export default {
   components: { ModalFaq },
   data() {
      return {
         isOpenFAQ: false,
      };
   },
   methods: {
      toggleModalFAQ() {
         this.isOpenFAQ = !this.isOpenFAQ;
      },
   },
};
</script>
<style scoped lang="scss">
.div-faq {
   position: fixed;
   top: 130px;
   left: 30px;

   /* 반응형 크기 */
   @media all and (max-width: 321px) {
      & {
         left: 10px;
      }
   }
}

.btn-faq-wrapper {
   cursor: pointer;
   z-index: 20;

   width: 45px;
   height: 45px;
   border-radius: 50%;
   background-color: rgb(255, 255, 255);
   box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.1);

   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;

   svg {
      width: 50%;
      height: 50%;
   }
}
</style>
