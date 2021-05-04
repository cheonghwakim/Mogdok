<template>
<div>
  <v-app>
      <bgm/>
  </v-app>
</div>
</template>

<script>
import bgm from './components/bgm';

export default {
  name: 'App',

  components: {
    bgm,
  },
};
</script>
