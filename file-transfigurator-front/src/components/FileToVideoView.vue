<template>
  <div class="fileToVideoView">
    <v-card class="mx-auto" width="500">
      <v-breadcrumbs>
        <v-breadcrumbs-item title="Home" @click="$emit('back')" class="breadcrumb-link" />
        <v-icon icon="mdi-chevron-right" />
        <v-breadcrumbs-item title="File to Video" :disabled="true" />
      </v-breadcrumbs>
      <div class="fileToVideoView__body">
        <v-file-input v-model="files" label="File to be converted to video"></v-file-input>
        <v-progress-linear v-model="progress" :max="progressMax" color="primary" height="10" v-show="!displayVideo" />
        <video v-show="displayVideo" controls class="fileToVideoView__video" ref="video" />
      </div>
      <v-card-actions class="pt-0">
        <v-spacer />
        <v-btn variant="text" color="primary" @click="transfigure">
          Transfigure
        </v-btn>
        <v-btn variant="text" @click="$emit('back')">
          Cancel
        </v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script lang="ts">
import FileToVideoTransfigurator from '@/core/FileToVideoTransfigurator';
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'FileToViewView',
  data: () => ({
    breadcrumbs: [
      {
        title: 'Home'
      }
    ],
    progress: 0,
    progressMax: 100,
    displayVideo: false,
    files: undefined
  }),
  methods: {
    async transfigure() {
      const transfigurator = new FileToVideoTransfigurator((this.files as any)[0]);
      transfigurator.onprogress = evt => {
        this.progressMax = evt.total;
        this.progress = evt.progress;
      };
      const video = await transfigurator.transfigure();
      const src = URL.createObjectURL(video!);
      (this.$refs.video as any).src = src;
      this.displayVideo = true;
    }
  }
});
</script>

<style>
.breadcrumb-link {
  cursor: pointer;
}

.breadcrumb-link:hover {
  text-decoration: underline;
}

.fileToVideoView__body {
  margin: 16px;
}

.fileToVideoView {
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.fileToVideoView__video {
  width: 100%;
}
</style>