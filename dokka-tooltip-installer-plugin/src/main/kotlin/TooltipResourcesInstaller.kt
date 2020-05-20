package org.virtuslab.dokka.tooltipGenerator

import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.transformers.pages.PageTransformer

object TooltipResourcesInstaller : PageTransformer {
    object TooltipCssResource : RendererSpecificPage {
        override val children: List<PageNode> = emptyList()
        override val name: String = "../scripts/tooltips.css"
        override val strategy: RenderingStrategy = RenderingStrategy.Write("""
            |/* Tooltip container */
            |.tooltip {
            |  position: relative;
            |  display: inline-block;
            |  border-bottom: 1px dotted black;
            |}
            |
            |/* Tooltip text */
            |.tooltip .tooltiptext {
            |  visibility: hidden;
            |  background-color: #555;
            |  color: #fff;
            |  text-align: center;
            |  padding: 5px 5px 5px 5px;
            |  border-radius: 6px;
            |
            |  /* Position the tooltip text */
            |  position: absolute;
            |  z-index: 1;
            |  bottom: 125%;
            |  left: 50%;
            |  margin-left: -60px;
            |
            |  /* Fade in tooltip */
            |  opacity: 0;
            |  transition: opacity 0.3s;
            |}
            |
            |/* Tooltip arrow */
            |.tooltip .tooltiptext::after {
            |  content: "";
            |  position: absolute;
            |  top: 100%;
            |  left: 50%;
            |  margin-left: -5px;
            |  border-width: 5px;
            |  border-style: solid;
            |  border-color: #555 transparent transparent transparent;
            |}
            |
            |/* Tooltip text when mouse over the tooltip container */
            |.tooltip:hover .tooltiptext {
            |  visibility: visible;
            |  opacity: 1;
            |}""".trimMargin("|"))

        override fun modified(name: String, children: List<PageNode>): PageNode = this
    }

    override fun invoke(input: RootPageNode): RootPageNode = appendResources(input).let {
        it.modified(children = it.children + TooltipCssResource) as RootPageNode
    }

    private fun appendResources(input: PageNode): PageNode = if (input is ContentPage)
        input.modified(embeddedResources = input.embeddedResources + "scripts/tooltips.css", children = input.children.map { appendResources(it) })
    else
        input.modified(children = input.children.map(::appendResources))
}