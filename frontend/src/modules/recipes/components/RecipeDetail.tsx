import { useNavigate, useParams } from "react-router-dom"
import {
	IconButton,
	Card,
	List,
	ListItemText,
	Stack,
	Typography,
	ListItem,
	ListSubheader,
	Divider,
	Rating,
	styled
} from "@mui/material"
import RestaurantIcon from "@mui/icons-material/Restaurant"
import KitchenIcon from "@mui/icons-material/Kitchen"
import { useEffect, useMemo, useState } from "react"
import { useGroceries, useRecipe, useSaveRecipeRating } from "../recipe-queries"
import { RecipeCommentList } from "./RecipeCommentList"
import { SaveRecipeCommentForm } from "./SaveRecipeCommentForm"
import { useSnackbar } from "notistack"
import { RecipeAverageRating } from "./RecipeAverageRating"
import ArrowBackIcon from "@mui/icons-material/ArrowBack"
import { TextStack } from "../../styles/containers"

interface RecipeItemDetail {
	id: number
	name: string
	category: string
	quantity: number
	unit: string
}

export const RecipeDetail = () => {
	const { id } = useParams()
	const navigate = useNavigate()
	const { enqueueSnackbar } = useSnackbar()

	const { groceries } = useGroceries()
	const { recipe } = useRecipe(Number(id))

	const recipeItems = useMemo(() => {
		if (!recipe) {
			return [] as RecipeItemDetail[]
		}

		return recipe.items.map(item => {
			const grocery = groceries?.find(grocery => grocery.id === item.grocery)

			const result: RecipeItemDetail = {
				id: item.grocery,
				quantity: item.quantity,
				unit: item.unit,
				name: grocery?.name ?? "unknown",
				category: grocery?.category ?? "unknown"
			}

			return result
		})
	}, [recipe, groceries])

	const { saveRecipeRatingAsync, isLoading: isSavingRating } = useSaveRecipeRating()

	const handleRatingClick = (value: number | null) => {
		if (!recipe || !value) {
			return
		}

		saveRecipeRatingAsync({ recipe: recipe.id, rating: value })
			.then(() => {
				enqueueSnackbar("Rating saved", { variant: "success" })
			})
	}

	const [ratingValue, setRatingValue] = useState(0)

	useEffect(() => {
		if (recipe) {
			setRatingValue(recipe.currentUserRating)
		}
	}, [recipe])

	return (
		<>
			<Stack spacing={2}>
				<RecipeCard>
					<Stack spacing={2}>
						<div>
							<IconButton onClick={() => navigate(-1)} ><ArrowBackIcon /></IconButton>
						</div>
						<TextStack spacing={2}>
							<Typography variant="h4">{recipe?.name}</Typography>
							<RecipeAverageRating recipe={recipe} />
						</TextStack>
						<TextStack>
							<RestaurantIcon/>
							<Typography>Portions: {recipe?.portions}</Typography>
						</TextStack>
						<Divider/>
						<List
							dense
							subheader={
								<ListSubheader>
									<TextStack>
										<KitchenIcon/>
										<Typography>Ingredients</Typography>
									</TextStack>
								</ListSubheader>
							}
						>
							{recipeItems.map((item, i) => (
								<ListItem key={i}>
									<ListItemText primary={`${item.name}: ${item.quantity} ${item.unit}`}/>
								</ListItem>
							))}
						</List>
						<Divider/>
						<Typography variant="h5">Instructions</Typography>
						<InstructionsTypography>{recipe?.instructions}</InstructionsTypography>
						<Divider />
						<TextStack>
							<Typography>Your rating</Typography>
							<Rating
								value={ratingValue}
								onChange={(_, value) => handleRatingClick(value)}
								readOnly={isSavingRating}
							/>
						</TextStack>
						{ recipe &&
							<SaveRecipeCommentForm recipeId={recipe.id} />
						}
						{ recipe &&
							<RecipeCommentList recipeId={recipe.id} />
						}
					</Stack>
				</RecipeCard>
			</Stack>
		</>
	)
}

const InstructionsTypography = styled(Typography)`
	white-space: pre-line;
`

const RecipeCard = styled(Card)`
  padding: 30px;
  width: 800px;
  align-self: center;
`