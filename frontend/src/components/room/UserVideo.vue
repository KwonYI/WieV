<template>
  <span v-if="streamManager" align="center" id="videoTemplate">
    <ov-video :stream-manager="streamManager" id="videoId" />
    <!-- <div > -->
    <p id="textId" style="margin:0px">{{ JSON.parse(streamManager.stream.connection.data.split("%/%")[0])['name'] }}</p>
    <!-- </div> -->
  </span>
</template>

<script>
import OvVideo from "./OvVideo";

export default {
  name: "UserVideo",

  components: {
    OvVideo,
  },

  props: {
    streamManager: Object,
  },

  data() {
    return {
      clientData : undefined
    }
  },

  created() {
    if(this.streamManager){
      const { connection } = this.streamManager.stream;
      this.clientData = JSON.parse(connection.data.split("%/%")[0]);
    }
  },
};
</script>
<style>
.speaking{
  border-color: skyblue;
  border-style: solid;
}

/* #videoTemplate{
  position: relative;
}


#textId {
  background-color: aliceblue;
}
#videoTemplate div{
    position: absolute;
    top: -9px;
    right : 0px;

} */
</style>
