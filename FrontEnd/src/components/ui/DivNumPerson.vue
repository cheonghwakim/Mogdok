<template lang="">
   <div class="NumPerson">
      <i class="fas fa-users"></i><span>{{ current }}/{{ limit }}</span>
   </div>
</template>

<script>
export default {
   props: {
      current: Number,
      limit: Number,
   },
};
</script>

<style scoped lang="scss">
$gray: rgb(84, 84, 84);

.NumPerson {
   width: 60px;
   height: 20px;

   border: 2px solid $gray;
   border-radius: 10px;
   padding: 0px 5px;

   display: flex;
   align-items: center;
   justify-content: space-between;

   color: $gray;

   // 유저 아이콘
   i {
      font-size: 8pt;
   }

   // 인원수 표시
   span {
      font-size: 8pt;
      font-weight: 800;
      margin-top: 2px;
   }
}
</style>
