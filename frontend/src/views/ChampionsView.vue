<script setup>
    import { ref, onMounted } from 'vue'
    import { useRouter } from 'vue-router'
    import { useAuthStore } from '../stores/auth'

    const router = useRouter()
    const authStore = useAuthStore()
    const champions = ref([])

    const goToChampion = (id) => {
        router.push(`/champions/${id}`)
    }

    onMounted(() => {
        fetchChampions()
    })

    const fetchChampions = async () => {
        try {
            const reponse = await fetch('http://localhost:8080/api/skins/champions', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + authStore.token
                },
            }) 
            const data = await reponse.json()
            console.log('Réponse du serveur :', data)
            champions.value = data
        } catch (error) {
            console.error('Erreur lors de la récupération de la garde-robe :', error)
        }
    }
</script>

<template>
        <h1>Champions</h1>
    <p>Voici la liste des champions !</p>
    <div class="champions-container">
        <div v-for="champion in champions" :key="champion.id" class="champion-card" @click="goToChampion(champion.id)">
            <img :src="champion.imageUrl" alt="">
            <p class="champion-name">{{ champion.name }}</p>
        </div>
    </div>
</template>

<style scoped>
    .champions-container {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
    }

    .champion-card {
        width: 200px;
        border: 1px solid #ccc;
        border-radius: 8px;
        padding: 10px;
        text-align: center;
        cursor: pointer;
        transition: transform 0.2s;
    }

    .champion-card:hover {
        transform: scale(1.05);
    }

    .champion-card img {
        width: 100%;
        border-radius: 4px;
    }

    .champion-name {
        font-weight: bold;
        margin: 10px 0 5px 0;
    }
</style>
