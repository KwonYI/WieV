import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/views/main/Home.vue'
import NotFound from '@/views/main/NotFound.vue'
import Profile from '@/views/accounts/Profile'
import ProfileUpdate from '@/views/accounts/ProfileUpdateForm'
import Signup from '@/views/accounts/Signup'
import Menu from '@/views/viewset/Menu'
import CreateSet from '@/views/viewset/CreateSet'
// import Viewees from '@/views/viewset/Viewees'
// import Viewers from '@/views/viewset/Viewers'
// import Progress from '@/views/viewset/Progress'
import Main from '@/views/main/Main'
import WaitRoom from '@/views/room/WaitRoom'
import ViewRoom from '@/views/room/ViewRoom'
import FindPassword from '@/views/accounts/FindPassword'

// import Recruit from '@/components/viewset/Recruit'

Vue.use(VueRouter)


const routes = [{
    path: '/main',
    name: 'Main',
    component: Main,
  },
  {
    path: '/404',
    name: 'notFound',
    component: NotFound
  },
  {
    path: "*",
    redirect:"/404"
  },
  {
    path: '/waitroom',
    name: 'WaitRoom',
    component: WaitRoom,
  },
  {
    path: '/viewroom',
    name: 'ViewRoom',
    component: ViewRoom,
  },
  {
    path: '/signup',
    name: 'Signup',
    component: Signup,
  },
  {
    path: 'profile',
    name: 'Profile',
    component: Profile,
  },
  {
    path: 'profileUpdateForm',
    name: 'ProfileUpdateForm',
    component: ProfileUpdate,
  },
  {
    path: '/menu',
    name: 'Menu',
    component: Menu,
    // children: [
    //   {
    //     path: '',
    //     redirect: 'progress',
    //   },
    //   {
    //     path: 'progress',
    //     name: 'Progress',
    //     component: Progress,
    //   },


    //   {
    //     path: 'viewees',
    //     name: 'Viewees',
    //     component: Viewees,
    //   },
    //   {
    //     path: 'viewers',
    //     name: 'Viewers',
    //     component: Viewers,
    //   },

    // ]
  },
  {
    path: '/createset',
    name: 'CreateSet',
    component: CreateSet
  },
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/findpassword',
    name: 'FindPassword',
    component: FindPassword
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
  // routes :[
  //   { path: '/menu/:recruitno', component : Progress}
  // ]
})


export default router