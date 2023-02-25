<template>
  <div class="homeView">
    <v-btn @click="fireInput('FILE_TO_VIDEO')" color="primary">File to Video</v-btn>
    <v-btn @click="fireInput('VIDEO_TO_FILE')">Video to File</v-btn>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'HomeView',
  methods: {
    fireInput(value: string) {
      this.$emit('input', value);
    }
  }
});
</script>

<style scoped>
.homeView {
  display: flex;
  width: 100%;
  height: 100%;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 16px;
}
</style>
