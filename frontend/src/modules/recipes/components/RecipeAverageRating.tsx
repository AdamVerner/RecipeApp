import { Recipe } from "../recipe-models"
import { Rating, Stack, Typography } from "@mui/material"

interface RecipeAverageRatingProps {
	recipe?: Recipe
}

export const RecipeAverageRating = ({ recipe }: RecipeAverageRatingProps) => {
	return (
		<Stack direction="row" spacing={1}>
			{ recipe && recipe.averageRating !== 0 &&
				<Typography>{recipe?.averageRating.toFixed(1)}</Typography>
			}
			<Rating name="averageRating" readOnly value={recipe?.averageRating ?? 0} precision={0.5} />
		</Stack>
	)
}