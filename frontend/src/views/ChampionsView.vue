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
    <v-container fluid class="pa-6">
        <div class="text-center mb-8">
            <h1 class="text-h3 font-weight-bold text-gradient mb-2">Champions</h1>
            <p class="text-body-1 text-medium-emphasis">Sélectionne un champion pour découvrir ses skins</p>
        </div>

        <v-row justify="center">
            <v-col
                v-for="champion in champions"
                :key="champion.id"
                cols="6"
                sm="4"
                md="3"
                lg="2"
                xl="2"
            >
                <v-lazy
                    :min-height="200"
                    :options="{ threshold: 0.1 }"
                    transition="fade-transition"
                >
                    <v-card
                        class="champion-card"
                        @click="goToChampion(champion.id)"
                    >
                        <div class="card-image-container">
                            <v-img
                                :src="champion.imageUrl"
                                :alt="champion.name"
                                class="card-image"
                            >
                                <template v-slot:placeholder>
                                    <div class="d-flex align-center justify-center fill-height">
                                        <v-progress-circular indeterminate color="primary" />
                                    </div>
                                </template>
                            </v-img>
                            <div class="card-overlay"></div>
                            <div class="card-content">
                                <h3 class="champion-name">{{ champion.name }}</h3>
                            </div>
                            <div class="card-glow"></div>
                        </div>
                    </v-card>
                </v-lazy>
            </v-col>
        </v-row>
    </v-container>
</template>

<style scoped>
    .text-gradient {
        background: linear-gradient(135deg, #c9aa71 0%, #f0e6d3 50%, #c9aa71 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
    }

    .champion-card {
        cursor: pointer;
        border-radius: 12px;
        overflow: hidden;
        background: transparent;
        transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
        border: 1px solid rgba(201, 170, 113, 0.2);
    }

    .champion-card:hover {
        transform: translateY(-8px) scale(1.02);
        border-color: rgba(201, 170, 113, 0.6);
        box-shadow:
            0 20px 40px rgba(0, 0, 0, 0.4),
            0 0 30px rgba(201, 170, 113, 0.2);
    }

    .card-image-container {
        position: relative;
        overflow: hidden;
    }

    .card-image {
        transition: transform 0.5s ease;
    }

    .champion-card:hover .card-image {
        transform: scale(1.1);
    }

    .card-overlay {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 60%;
        background: linear-gradient(
            to top,
            rgba(0, 0, 0, 0.9) 0%,
            rgba(0, 0, 0, 0.6) 40%,
            transparent 100%
        );
        pointer-events: none;
    }

    .card-content {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        padding: 16px;
        text-align: center;
    }

    .champion-name {
        font-size: 1.1rem;
        font-weight: 700;
        color: #fff;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
        letter-spacing: 0.5px;
        transition: color 0.3s ease;
    }

    .champion-card:hover .champion-name {
        color: #c9aa71;
    }

    .card-glow {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        opacity: 0;
        background: linear-gradient(
            135deg,
            rgba(201, 170, 113, 0.1) 0%,
            transparent 50%,
            rgba(201, 170, 113, 0.1) 100%
        );
        transition: opacity 0.4s ease;
        pointer-events: none;
    }

    .champion-card:hover .card-glow {
        opacity: 1;
    }
</style>
