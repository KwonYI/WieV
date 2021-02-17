<template>
  <div id="home">

    <v-row justify="center" class="pt-10 main-bg-navy-img" style="height: 100vh; position: relative" no-gutters>
      <v-col cols="8" class="main-box text-center" style="z-index:1">
        <div class="d-flex flex-column justify-center align-center text-white">
          <v-col class="wiev-box">
            <v-col :class="[getAccessToken ? '' : 'text-h5']">
              <h3 class="white--text" data-aos="fade-in">View Everywhere</h3>
            </v-col>
            <v-col v-if="!getAccessToken">
              비대면 화상면접 플랫폼
            </v-col>
            <img class="mt-3" v-if="!getAccessToken" :src="images.logo" width="200" alt="logo">
            <p :class="[getAccessToken ? 'text-h4' : 'text-h3']" class="mt-3">WieV</p>
            

          </v-col>

          <!-- 로그인 시, 면접스케줄 현황 -->
          <v-col v-if="getAccessToken" class="mt-1">
            <!-- 로그인 완료 -->
            <div v-if="getUserViewWait === -1">
              <Schedule />
            </div>
            <div v-else>
              <!-- <v-btn class="m-2 white--text" elevation="2" large rounded color=#304B61 :to="{ name: 'Main', params: { isLogin: true } }"> -->
              <v-btn class="m-2 amber lighten-3 black--text font-weight-black" elevation="2" x-large rounded  :to="{ name: 'Main', params: { isLogin: true } }">
                대기실 이동하기
              </v-btn>
            </div>
          </v-col>
        </div>
      </v-col>
    </v-row>


    <!-- <v-parallax
      dark
      :src="require('@/assets/images/bg_image_dark_61.png')"
      height="700"
    >
    </v-parallax> -->

    <Introduce />
    <Ask />

    <v-fab-transition>
      <v-btn
        bottom
        right
        fixed
        fab
        dark
        small
        v-show="btnShow"
        @click="$vuetify.goTo(0)"
        style="display: initial"
      >
        <!-- <v-icon>fas fa-angle-double-up</v-icon> -->
        눌러
      </v-btn>
    </v-fab-transition>
    <div style="cursor:pointer;" @click="$vuetify.goTo(0)">또 눌러</div>
  </div>

</template>



<script>
import AOS from 'aos'
import 'aos/dist/aos.css'

AOS.init()

  import {
    mapGetters
  } from "vuex";

  import Schedule from "@/components/main/Schedule";
  import Ask from '@/components/main/Ask'
  import Introduce from '@/components/main/Introduce'
  import anime from 'animejs/lib/anime.es.js'




  export default {
    name: "Home",
    components: {
      Schedule,
      Ask,
      Introduce
    },
    data: () => ({
      message: "",
      images: {
        logo: require('@/assets/images/new_logo_shadow.png')
      },
    }),
    computed: {
      ...mapGetters(["getUser", "getAccessToken", "getUserViewWait"]),
    },
    methods: {
      handleScroll() {
        this.btnShow = window.scrollY > 400
      }
    },
    beforeMount() {
      window.addEventListener('scroll', this.handleScroll)
    },
    beforeDestroy() {
      window.addEventListener('scroll', this.handleScroll)
    },
    created: {

    },
    mounted() {
      var textWrapper = document.querySelector('.ml2');
      textWrapper.innerHTML = textWrapper.textContent.replace(/\S/g, "<span class='letter'>$&</span>");

      anime.timeline({
          loop: true
        })
        .add({
          targets: '.ml2 .letter',
          scale: [4, 1],
          opacity: [0, 1],
          translateZ: 0,
          easing: "easeOutExpo",
          duration: 1300,
          delay: (el, i) => 70 * i
        }).add({
          targets: '.ml2',
          opacity: 0,
          duration: 1000,
          easing: "easeOutExpo",
          delay: 1500
        });

    

    },
  }
</script>

<style scoped>
  tr * {
    padding: 0;
    /* margin-bottom: 2rem; */
  }

  table tbody tr:hover {
    background: inherit !important;
  }

  td {
    border: none !important;
  }


  
</style>