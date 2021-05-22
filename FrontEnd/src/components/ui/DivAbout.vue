<template lang="">
   <div class="div-about">
      <div v-wave class="btn-about-wrapper" @click="toggleAbout">
         <svg width="100%" height="100%" viewBox="0 0 22 34" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path
               d="M13.86 26.6601C13.4 28.2301 13.01 29.4601 12.69 30.7201C12.26 32.3701 11.43 33.6801 9.54001 33.3001C7.61001 32.9101 7.81001 31.2801 8.03001 29.8101C8.11001 29.2801 8.17001 28.7101 8.41001 28.2501C10.01 25.1701 10.46 22.2101 7.95001 19.3101C7.63001 18.9401 7.55001 18.0301 7.77001 17.5801C9.10001 14.9101 11.76 15.4701 13.98 15.4501C15.06 15.4401 16.2 16.4901 17.21 17.2201C17.51 17.4301 17.67 18.3601 17.46 18.6501C15.24 21.7101 17.64 23.5901 19.36 25.3501C20.81 26.8401 21.86 28.3501 20.19 29.8501C18.48 31.3901 16.69 30.6501 15.52 28.6201C15.17 28.0101 14.61 27.5301 13.86 26.6601Z"
               fill="black"
            />
            <path
               d="M20.88 7.25995C20.55 8.16995 20.3 9.87995 19.36 10.9899C18.82 11.6299 16.68 11.8299 16.04 11.2899C13.46 9.11995 12.76 5.92995 13.12 2.75995C13.23 1.78995 14.97 0.399947 16.07 0.289947C17.98 0.0899469 20.16 0.499946 20.6 3.07995C20.79 4.21995 20.77 5.39995 20.88 7.25995Z"
               fill="black"
            />
            <path
               d="M7.48999 7.42994C7.41999 8.59994 7.59999 9.44993 7.27999 9.99994C6.63999 11.0599 5.84999 12.4999 4.85999 12.7499C4.00999 12.9599 2.16999 11.9699 1.86999 11.1099C1.06999 8.88994 0.579994 6.44994 0.589994 4.09994C0.599994 3.14994 2.17999 1.51994 3.13999 1.44994C4.22999 1.35994 5.82999 2.41994 6.48999 3.43994C7.24999 4.63994 7.23999 6.33994 7.48999 7.42994Z"
               fill="black"
            />
         </svg>
      </div>

      <modal-about :isOpenAbout="isOpenAbout" @onClick="closeModalAbout"></modal-about>
   </div>
</template>
<script>
import ModalAbout from '@/components/ui/ModalAbout';

export default {
   components: { ModalAbout },
   data() {
      return {
         isOpenAbout: false,
      };
   },
   methods: {
      toggleAbout: function() {
         this.isOpenAbout = !this.isOpenAbout;
      },
      closeModalAbout: function() {
         this.isOpenAbout = false;
      },
   },
};
</script>
<style scoped lang="scss">
.div-about {
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

.btn-about-wrapper {
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
