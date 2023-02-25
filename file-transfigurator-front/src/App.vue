<template>
  <v-app>
    <v-main>
      <HomeView v-if="view === 'HOME'" @input="changeView"/>
      <FileToVideoView v-if="view==='FILE_TO_VIDEO'" @back="changeView('HOME')"/>
    </v-main>
  </v-app>
</template>

<script lang="ts">
import HomeView from '@/components/HomeView.vue';
import FileToVideoView from '@/components/FileToVideoView.vue';
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'App',
  components: {
    HomeView,
    FileToVideoView
  },
  data: ()=>({
    view: 'HOME'
  }),
  methods: {
    changeView(newView: string) {
      this.view = newView;
    }
  }
});
</script>
