import { Card, CardContent, Typography, Grid, Button, Divider, Stack, CardMedia } from "@mui/material"
import { useNavigate } from "react-router-dom"
import { AppRoutes } from "../../RootRouter"
import { Recipe } from "../recipe-models"
import { RecipeAverageRating } from "./RecipeAverageRating"
import DefaultRecipeImage from "../../../assets/images/recipe-image-default.png"

export interface RecipeListProps {
	recipes: Recipe[]
}

export const RecipeList = ({ recipes }: RecipeListProps) => {
	const navigate = useNavigate()

	return (
		<Grid container spacing={3} justifyContent="center" sx={{ maxWidth: 1000 }}>
			{recipes.map((recipe, i) => (
				<Grid item key={i} md={4} sm={12}>
					<Card sx={{ minWidth: 275 }}>

						<CardMedia
							component="img"
							height="194"
							image={recipe.imageUrl ?? DefaultRecipeImage}
							alt={recipe.name}
							onClick={() => navigate(`${AppRoutes.RecipeDetailRoute}/${recipe.id}`)}
						/>

						<CardContent>
							<Stack spacing={1}>
								<Typography variant="h6">{recipe.name}</Typography>
								<Divider />
								<Stack direction="row" justifyContent="space-between">
									<RecipeAverageRating recipe={recipe} />
									<Button size="small"  onClick={() => navigate(`${AppRoutes.RecipeDetailRoute}/${recipe.id}`)}>
										See more
									</Button>
								</Stack>
							</Stack>
						</CardContent>
					</Card>
				</Grid>
			))}
		</Grid>
	)
}
