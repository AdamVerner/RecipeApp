import { Routes, Route } from "react-router-dom"
import { RecipeDetail } from "./recipes/components/RecipeDetail"
import { useRecipeStore } from "./recipes/recipe-store"
import { useEffect, useState } from "react"
import { RecipeBrowser } from "./recipes/components/RecipeBrowser"

export enum AppAuthRoutes {
	UserRecipesRoute = "/",
	AllRecipesRoute = "/recipes",
	RecipeDetailRoute = "/recipes"
}

export const AuthRouter = () => {
	const recipeStore = useRecipeStore()

	const [isFetching, setIsFetching] = useState(false)

	useEffect(() => {
		if (!recipeStore.initialized && !isFetching) {
			setIsFetching(true)
			recipeStore.fetchAll()
				.finally(() => setIsFetching(false))
		}
	}, [recipeStore, isFetching])

	return (
		<Routes>
			<Route path={AppAuthRoutes.UserRecipesRoute} element={<RecipeBrowser recipes={recipeStore.userRecipes} />}/>
			<Route path={AppAuthRoutes.AllRecipesRoute} element={<RecipeBrowser recipes={recipeStore.allRecipes} />}/>
			<Route path={`${AppAuthRoutes.RecipeDetailRoute}/:id`} element={<RecipeDetail/>}/>
		</Routes>
	)
}
