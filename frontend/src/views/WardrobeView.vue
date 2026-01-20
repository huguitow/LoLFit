<script setup>
    import { ref, onMounted } from 'vue'
    import { useAuthStore } from '../stores/auth'
    const authStore = useAuthStore()
    const skins = ref([])

    onMounted(() => {
        fetchWardrobe()
    })

const fetchWardrobe = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/wardrobe', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + authStore.token
                },
            })
            const data = await response.json()
            console.log('Réponse du serveur :', data)
            skins.value = data
        } catch (error) {
            console.error('Erreur lors de la récupération de la garde-robe :', error)
        }
    }

</script>

<template>
    <h1>Ma Garde-Robe</h1>
    <p>Bienvenue dans ta collection de skins !</p>
    <div class="skins-container">
        <div v-for="skin in skins" :key="skin.id" class="skin-card">
            <img :src="skin.imageUrl" alt="">
            <p class="skin-name">{{ skin.skinName }}</p>
            <p class="champion-name">{{ skin.championName }}</p>
        </div>
    </div>
</template>

<style scoped>
.skins-container {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
}

.skin-card {
    width: 200px;
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 10px;
    text-align: center;
}

.skin-card img {
    width: 100%;
    border-radius: 4px;
}

.skin-name {
    font-weight: bold;
    margin: 10px 0 5px 0;
}

.champion-name {
    color: #666;
    font-size: 14px;
}
</style>