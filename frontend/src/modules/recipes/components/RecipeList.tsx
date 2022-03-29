import { Card, CardContent, Typography, Grid, CardActions, Button, Divider } from "@mui/material"
import { useNavigate } from "react-router-dom"
import { AppRoutes } from "../../RootRouter"
import { Recipe } from "../recipe-models"

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
						<CardContent>
							<Typography variant="h6">{recipe.name}</Typography>
							<Divider />
							<Typography variant="body2">Portions: {recipe.portions}</Typography>
							<Typography variant="body1" sx={{ whiteSpace: "pre-line" }}>{recipe.instructions}</Typography>
						</CardContent>
						<CardActions>
							<Button size="small" onClick={() => navigate(`${AppRoutes.RecipeDetailRoute}/${recipe.id}`)}>
								See more
							</Button>
						</CardActions>
					</Card>
				</Grid>
			))}
		</Grid>
	)
}
