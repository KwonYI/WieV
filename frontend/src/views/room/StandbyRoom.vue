<template>
    <div id="standbyRoom">
        <!-- 대기실 페이지. 신분에 따라 보여지는 컴포넌트가 다르다.  -->
         <div>
    <v-app-bar
      color="deep-purple accent-4"
      dense
      dark
    >

      <v-toolbar-title>Page title</v-toolbar-title>

      <v-spacer></v-spacer>

    </v-app-bar>
    sadfdasf
  </div>



    </div>
</template>

<script>
    import {
        mapState
    } from "vuex";

    // import Schedule from '@/components/main/Schedule'

    export default {
        name: "StandbyRoom",
        components: {


        },
        data: function () {
            return {
                temp_list: [1, 2],
                com_logo: "https://r1.community.samsung.com/t5/image/serverpage/image-id/616190iB4F850C825C2D0CD/image-size/large?v=1.0&px=999",

            };
        },

        methods: {},
        created: function () {
            // if (this.viewerLogin) {
            //   this.$router.push({ name: "ViewerRecruitItem" });
            // }
        },

        computed: {
            ...mapState(["whoLogin"]),
        },
    };
</script>

<style scoped>
#standbyRoom{
    background-color: white;
    width: 100vw;
    height: 100vh;
}
</style>