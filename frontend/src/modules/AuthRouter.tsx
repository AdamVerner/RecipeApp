import { Routes, Route } from "react-router-dom"
import { RecipeDetail } from "./recipes/components/RecipeDetail"
import { RecipeBrowser } from "./recipes/components/RecipeBrowser"
import { useAllRecipes, useUserRecipes } from "./recipes/recipe-queries"
import { Pantry } from "./pantry/components/Pantry"

export enum AppAuthRoutes {
	HomeRoute = "/",
	UserRecipesRoute = "/user-recipes",
	AllRecipesRoute = "/all-recipes",
	RecipeDetailRoute = "/recipes",
	PantryRoute = HomeRoute
}

export const AuthRouter = () => {
	const { recipes: userRecipes } = useUserRecipes()
	const { recipes: allRecipes } = useAllRecipes()

	return (
		<>
			<Routes>
				<Route path={AppAuthRoutes.UserRecipesRoute} element={<RecipeBrowser recipes={userRecipes} />}/>
				<Route path={AppAuthRoutes.AllRecipesRoute} element={<RecipeBrowser recipes={allRecipes} />}/>
				<Route path={`${AppAuthRoutes.RecipeDetailRoute}/:id`} element={<RecipeDetail/>}/>
				<Route path={AppAuthRoutes.PantryRoute} element={<Pantry />}/>
			</Routes>
		</>
	)
}
