import { Routes, Route } from "react-router-dom"
import { RecipeDetail } from "./recipes/components/RecipeDetail"
import { RecipeBrowser } from "./recipes/components/RecipeBrowser"
import { useAllRecipes, useUserRecipes } from "./recipes/recipe-queries"

export enum AppAuthRoutes {
	UserRecipesRoute = "/",
	AllRecipesRoute = "/recipes",
	RecipeDetailRoute = "/recipes"
}

export const AuthRouter = () => {
	const userRecipes = useUserRecipes()
	const allRecipes = useAllRecipes()

	return (
		<Routes>
			<Route path={AppAuthRoutes.UserRecipesRoute} element={<RecipeBrowser recipes={userRecipes.data ?? []} />}/>
			<Route path={AppAuthRoutes.AllRecipesRoute} element={<RecipeBrowser recipes={allRecipes.data ?? []} />}/>
			<Route path={`${AppAuthRoutes.RecipeDetailRoute}/:id`} element={<RecipeDetail/>}/>
		</Routes>
	)
}
