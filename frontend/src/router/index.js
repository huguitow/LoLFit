import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import WardrobeView from '../views/WardrobeView.vue'
import ChampionsView from '../views/ChampionsView.vue'
import ChampionDetailView from '../views/ChampionDetailView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/champions'
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/wardrobe',
      name: 'wardrobe',
      component: WardrobeView
    },
    {
      path: '/champions',
      name: 'champions',
      component: ChampionsView
    },
    {
      path: '/champions/:id',
      name: 'champion-detail',
      component: ChampionDetailView
    }
  ],
})

export default router
