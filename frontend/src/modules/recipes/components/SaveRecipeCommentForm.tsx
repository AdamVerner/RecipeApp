import { useForm } from "react-hook-form"
import { useSaveRecipeComment } from "../recipe-queries"
import { RecipeCommentFormData, RecipeCommentSchema } from "../recipe-schemas"
import { yupResolver } from "@hookform/resolvers/yup"
import { useSnackbar } from "notistack"
import { Button, TextField, Stack, IconButton } from "@mui/material"
import EmojiPicker, { IEmojiData } from "emoji-picker-react"
import InsertEmoticonIcon from "@mui/icons-material/InsertEmoticon"
import { useRef, useState } from "react"

interface SaveRecipeCommentFormProps {
	recipeId: number
}

export const SaveRecipeCommentForm = ({ recipeId }: SaveRecipeCommentFormProps) => {
	const { register, handleSubmit, formState: { errors }, getValues, setValue, reset }
		= useForm<RecipeCommentFormData>({ resolver: yupResolver(RecipeCommentSchema) })

	const { saveRecipeCommentAsync } = useSaveRecipeComment()
	const { enqueueSnackbar } = useSnackbar()
	const [showEmoji, setShowEmoji] = useState(false)

	const textRegister = register("text")
	const textRef = useRef<HTMLInputElement | null>(null)

	const onEmojiClick = (emojiObject: IEmojiData) => {
		if (!textRef.current) {
			return
		}

		const cursor = textRef.current?.selectionStart ?? 0
		const text = getValues("text")

		const newText = text.slice(0, cursor) + emojiObject.emoji + text.slice(cursor)
		setValue("text", newText)

		const newCursor = cursor + emojiObject.emoji.length
		setTimeout(() => textRef.current?.setSelectionRange(newCursor,newCursor), 10)
	}

	const toggleShowEmoji = () => setShowEmoji(!showEmoji)

	const onSubmit = (data: RecipeCommentFormData) => {
		const request = {
			recipe: recipeId,
			text: data.text
		}

		saveRecipeCommentAsync(request)
			.then(() => {
				enqueueSnackbar("Comment saved", { variant: "success" })
				reset()
			})
	}

	return (
		<>
			<form onSubmit={handleSubmit(onSubmit)}>
				<Stack>
					<Stack spacing={3} direction="row">
						<TextField
							error={!!errors.text}
							helperText={errors.text?.message}
							{...textRegister}
							inputRef={textRef}
						/>
						<Button type="submit" variant="contained" sx={{ maxHeight: 40 }}>
						Send
						</Button>
					</Stack>
					<IconButton onClick={toggleShowEmoji} sx={{ alignSelf: "flex-start" }}>
						<InsertEmoticonIcon />
					</IconButton>
					{ showEmoji &&
						<EmojiPicker
							native
							onEmojiClick={(e, data) => onEmojiClick(data)}
						/>
					}
				</Stack>
			</form>
		</>
	)
}